package cn.edu.scau.acm.acmer.service.impl;

import cn.edu.scau.acm.acmer.entity.Contest;
import cn.edu.scau.acm.acmer.entity.OjAccount;
import cn.edu.scau.acm.acmer.entity.Problem;
import cn.edu.scau.acm.acmer.httpclient.HduClient;
import cn.edu.scau.acm.acmer.repository.ContestRepository;
import cn.edu.scau.acm.acmer.repository.OjAccountRepository;
import cn.edu.scau.acm.acmer.service.ContestService;
import cn.edu.scau.acm.acmer.service.HduService;
import cn.edu.scau.acm.acmer.service.ProblemService;
import org.apache.http.NameValuePair;
import org.apache.http.ProtocolException;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class HduServiceImpl implements HduService {

    Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private OjAccountRepository ojAccountRepository;

    @Autowired
    private ProblemService problemService;

    @Autowired
    private ContestService contestService;

    @Autowired
    private ContestRepository contestRepository;

    @Override
    public void hduLogout() {
        String url = "http://acm.hdu.edu.cn/userloginex.php?action=logout";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        ResponseEntity<String> response = restTemplate.postForEntity( url, request , String.class );
    }

    @Override
    public boolean checkHduAccount(String username, String password) {
        hduLogout();
        String url = "http://acm.hdu.edu.cn/userloginex.php?action=login";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        map.add("username", username);
        map.add("userpass", password);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        ResponseEntity<String> response = restTemplate.postForEntity( url, request , String.class );
        log.info(String.valueOf(response.getStatusCodeValue()));
        return response.getStatusCodeValue() == 302;
    }

    @Override
    public void getAcProblemsByHduAccount(OjAccount hduAccount) {
        String url = "http://acm.hdu.edu.cn/status.php?first=&pid=&user=" + hduAccount.getAccount() +"&lang=0&status=5";
        int retry = 10;
        while(url != null && retry > 0) {
            try {
                log.info(url);
                Document document = Jsoup.connect(url).get();
                Elements table = document.body().selectFirst("div#fixed_table").selectFirst("table").selectFirst("tbody").select("tr");
                for (int i = 1; i < table.size(); ++i) {
                    Elements line = table.get(i).select("td");
                    if (line.select("font").first().text().equals("Accepted")) {
                        String proId = line.get(3).selectFirst("a").text();
                        Timestamp time = Timestamp.valueOf(line.get(1).text());
                        problemService.addProblem("HDU", proId);
                        Problem problem = problemService.findProblem("HDU", proId);
                        if(!problemService.addProblemAcRecord(problem , hduAccount, time.getTime())) {
                            continue;
                        }
                    }
                }
                Elements links = document.body().selectFirst("p.footer_link").select("a");
                url = null;
                for (Element link : links) {
                    if (link.text().equals("Next Page >")) {
                        url = "http://acm.hdu.edu.cn/" + link.attr("href");
                    }
                }
                retry = 10;
            } catch (Exception e) {
                try {
                    Thread.sleep(1000);
                    retry--;
                    log.error("网络错误，准备重试，重试第{}次", 10-retry);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                e.printStackTrace();
            }
        }
    }

    @Override
    public void getAllAcProblems() {
        List<OjAccount> ojAccounts = ojAccountRepository.findAllByOjName("HDU");
        for(OjAccount ojAccount : ojAccounts) {
            getAcProblemsByHduAccount(ojAccount);
        }
    }

    @Override
    public void addContest(String ojId, String username, String password) throws Exception {
        HduClient hduClient = new HduClient();
        String url = "http://acm.hdu.edu.cn/contests/contest_show.php?cid=" + ojId;
        String html = hduClient.get(url);
        String title = Jsoup.parse(html).title();
        if(title.equals("User Login")) {
            if(username.equals("") || password.equals("")) {
                throw new Exception("该比赛需要登录");
            }
            loginContest(hduClient, ojId, username, password);
        }
        html = hduClient.get(url);
        Element element = Jsoup.parse(html);
        Contest contest = new Contest();
        contest.setOjName("HDU");
        contest.setOjid(ojId);
        contest.setName(element.selectFirst("h1").text());
        String time = element.selectFirst("div:contains(Start Time)").child(1).text();
        String startTime = time.substring(time.indexOf(':') + 2,time.indexOf('E')-1);
        time = time.substring(time.indexOf("End Time"));
        String endTime = time.substring(time.indexOf(':') + 2,time.indexOf('C')-1);
        contest.setStartTime(Timestamp.valueOf(startTime));
        contest.setEndTime(Timestamp.valueOf(endTime));
        contestRepository.save(contest);
        contest = contestRepository.findByOjNameAndOjid("HDU", ojId).get();
        Elements table = Jsoup.parse(html).selectFirst("tbody").select("tr");
        for (int i = 1; i < table.size(); ++i) {
            Element line = table.get(i);
            contestService.addContestProblem(contest.getId(),line.select("td").get(1).text(), null);
        }
    }

    @Override
    public void loginContest(HduClient hduClient, String cId, String username, String password) throws Exception {
        try {
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("username", username));
            params.add(new BasicNameValuePair("userpass", password));
            params.add(new BasicNameValuePair("login", "Sign In"));
            String result = hduClient.post("http://acm.hdu.edu.cn/userloginex.php?action=login&cid=" + cId + "&notice=0", params);
            if(Jsoup.parse(result).title().equals("User Login")) {
                throw new Exception("用户名或密码错误");
            }
        } catch (UnknownHostException e) {
            throw new Exception("无法访问acm.hdu.edu.cn，请稍后再试");
        }
    }
}
