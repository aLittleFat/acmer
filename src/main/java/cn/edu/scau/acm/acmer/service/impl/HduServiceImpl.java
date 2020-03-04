package cn.edu.scau.acm.acmer.service.impl;

import cn.edu.scau.acm.acmer.entity.*;
import cn.edu.scau.acm.acmer.httpclient.BaseHttpClient;
import cn.edu.scau.acm.acmer.repository.*;
import cn.edu.scau.acm.acmer.service.HduService;
import cn.edu.scau.acm.acmer.service.OJService;
import cn.edu.scau.acm.acmer.service.ProblemService;
import org.apache.http.NameValuePair;
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
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    private ContestRepository contestRepository;

    @Autowired
    private ContestProblemRepository contestProblemRepository;

    @Autowired
    private ContestRecordRepository contestRecordRepository;

    @Autowired
    private ContestProblemRecordRepository contestProblemRecordRepository;

    @Autowired
    private ProblemRepository problemRepository;

    @Autowired
    private OJService ojService;

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
    @Async
    @Transactional
    public void getAcProblemsByHduAccount(OjAccount hduAccount) {
        BaseHttpClient httpClient = new BaseHttpClient();
        String url = "http://acm.hdu.edu.cn/status.php?first=&pid=&user=" + hduAccount.getAccount() +"&lang=0&status=5";
        try {
            while(url != null) {
                Document document = Jsoup.parse(httpClient.get(url));
                Elements table = document.body().selectFirst("div#fixed_table").selectFirst("table").selectFirst("tbody").select("tr");
                for (int i = 1; i < table.size(); ++i) {
                    Elements line = table.get(i).select("td");
                    if (line.select("font").first().text().equals("Accepted")) {
                        String proId = line.get(3).selectFirst("a").text();
                        Timestamp time = Timestamp.valueOf(line.get(1).text());

                        if(problemRepository.findByOjNameAndProblemId("HDU", proId).isEmpty()) {
                            String title = Jsoup.parse(httpClient.get("http://acm.hdu.edu.cn/showproblem.php?pid=" + proId)).selectFirst("h1").text();
                            problemService.addProblem("HDU", proId, title);
                        }
                        Problem problem = problemService.findProblem("HDU", proId);
                        if (problemService.addProblemAcRecord(problem, hduAccount, time.getTime() + 8*60*60*1000)) {
                            break;
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
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Override
    @Async
    public void getAllAcProblems() {
        List<OjAccount> ojAccounts = ojAccountRepository.findAllByOjName("HDU");
        for(OjAccount ojAccount : ojAccounts) {
            getAcProblemsByHduAccount(ojAccount);
        }
    }

    @Override
    public void addContest(String ojName, String cId, String username, String password) throws Exception {
        BaseHttpClient httpClient = new BaseHttpClient();
        String url = "http://acm.hdu.edu.cn/contests/contest_show.php?cid=" + cId;
        log.info(url);
        String html = httpClient.get(url);
        String title = Jsoup.parse(html).title();
        Contest contest = new Contest();
        contest.setOjName(ojName);
        contest.setCid(cId);
        if(title.equals("User Login")) {
            if(username == null || username.equals("") || password == null || password.equals("")) {
                throw new Exception("该比赛需要登录");
            }
            loginContest(httpClient, cId, username, password);
            contest.setUsername(username);
            contest.setPassword(password);
        }
        html = httpClient.get(url);
        Element element = Jsoup.parse(html);
        contest.setTitle(element.selectFirst("h1").text());
        String time = element.selectFirst("div:contains(Start Time)").child(1).text();
        String startTime = time.substring(time.indexOf(':') + 2,time.indexOf('E')-1);
        time = time.substring(time.indexOf("End Time"));
        String endTime = time.substring(time.indexOf(':') + 2,time.indexOf('C')-1);
        contest.setStartTime(new Timestamp(Timestamp.valueOf(startTime).getTime() + 8*60*60*1000));
        contest.setEndTime(new Timestamp(Timestamp.valueOf(endTime).getTime() + 8*60*60*1000));
        if (System.currentTimeMillis() > contest.getEndTime().getTime()) {
            Elements table = Jsoup.parse(html).selectFirst("tbody").select("tr");
            contest.setProblemNumber(table.size() - 1);
        } else {
            contest.setProblemNumber(0);
        }
        contestRepository.save(contest);
    }

    @Override
    @Async
    public void updateContestProblem(BaseHttpClient httpClient, Contest contest) throws Exception {
//        for (int i = 1; i < table.size(); ++i) {
//            Element line = table.get(i);
//            ContestProblem contestProblem = new ContestProblem();
//            contestProblem.setContestId(contest.getId());
//            contestProblem.setProblemIndex(line.select("td").get(1).text());
//            contestProblemRepository.save(contestProblem);
//        }
    }

    @Override
    public void loginContest(BaseHttpClient httpClient, String cId, String username, String password) throws Exception {
//        log.info("登录");
        try {
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("username", username));
            params.add(new BasicNameValuePair("userpass", password));
            params.add(new BasicNameValuePair("login", "Sign In"));
            String result = httpClient.post("http://acm.hdu.edu.cn/userloginex.php?action=login&cid=" + cId + "&notice=0", params);
            if(Jsoup.parse(result).title().equals("User Login")) {
                throw new Exception("用户名或密码错误");
            }
        } catch (UnknownHostException e) {
            throw new Exception("无法访问acm.hdu.edu.cn，请稍后再试");
        }
    }

    @Override
    @Transactional
    public void addContestRecord(String ojName, String cId, String studentId, Integer teamId, String account, String password) throws Exception {
        BaseHttpClient httpClient = new BaseHttpClient();
        Contest contest = contestRepository.findByOjNameAndCid(ojName, cId).get();

        Optional<ContestRecord> optionalContestRecord = contestRecordRepository.findByContestIdAndStudentIdAndTeamId(contest.getId(), studentId, teamId);

        if(optionalContestRecord.isPresent()) {
            throw new Exception("已存在该竞赛记录");
        }

        if(contest.getPassword() != null) {
            loginContest(httpClient, contest.getCid(), contest.getUsername(), contest.getPassword());
        }

        if(contest.getEndTime().getTime() > System.currentTimeMillis()) {
            throw new Exception("比赛还未结束");
        }

        int maxPage = 1;
        Document document = Jsoup.parse(httpClient.get("http://acm.hdu.edu.cn/contests/contest_ranklist.php?cid=" + cId + "&page=" + 1));
        Elements links = document.select("a");
        for(Element link : links) {
            String linkText = link.text();
            if(linkText.length() == 0) continue;
            try{
                int num = Integer.parseInt(linkText);
                maxPage = Math.max(maxPage, num);
            } catch (Exception ignored){}
        }

        log.info(String.valueOf(maxPage));

        boolean hasTakeParkIn = false;

        for (int page = 1; page <= maxPage; ++page) {
            document = Jsoup.parse(httpClient.get("http://acm.hdu.edu.cn/contests/contest_ranklist.php?cid=" + cId + "&page=" + page));
            Elements table = document.selectFirst("tbody").select("tr");
            for (int i = 1; i < table.size(); ++i) {
                String handle = table.get(i).select("td").get(1).text().split(" ")[0];
                if(handle.equals(account)) {
                    hasTakeParkIn = true;
                    break;
                }
            }
            if (hasTakeParkIn) {
                break;
            }
        }

        if(!hasTakeParkIn) {
            throw new Exception("没有参加该比赛");
        }

        ContestRecord contestRecord = new ContestRecord();
        contestRecord.setTeamId(teamId);
        contestRecord.setAccount(account);
        contestRecord.setStudentId(studentId);
        contestRecord.setContestId(contest.getId());
        contestRecord.setTime(contest.getStartTime());
        contestRecordRepository.save(contestRecord);

        contestRecord = contestRecordRepository.findByContestIdAndStudentIdAndTeamId(contest.getId(), studentId, teamId).get();

        updateContestProblemRecord(httpClient, contestRecord);

    }

    @Async
    @Override
    @Transactional
    public void updateContestProblemRecord(BaseHttpClient httpClient, ContestRecord contestRecord) throws Exception {
        if (httpClient == null) {
            httpClient = new BaseHttpClient();
        }

        Contest contest = contestRepository.findById(contestRecord.getContestId()).get();

        if(System.currentTimeMillis() < contest.getEndTime().getTime()) {
            return;
        }

        String cId = contest.getCid();

        int maxPage = 1;
        Document document = Jsoup.parse(httpClient.get("http://acm.hdu.edu.cn/contests/contest_ranklist.php?cid=" + cId + "&page=" + 1));
        Elements links = document.select("a");
        for(Element link : links) {
            String linkText = link.text();
            if(linkText.length() == 0) continue;
            try{
                int num = Integer.parseInt(linkText);
                maxPage = Math.max(maxPage, num);
            } catch (Exception ignored){}
        }

        log.info(String.valueOf(maxPage));

        boolean hasTakeParkIn = false;

        for (int page = 1; page <= maxPage; ++page) {
            document = Jsoup.parse(httpClient.get("http://acm.hdu.edu.cn/contests/contest_ranklist.php?cid=" + cId + "&page=" + page));
            Elements table = document.selectFirst("tbody").select("tr");
            for (int i = 1; i < table.size(); ++i) {
                Elements tds = table.get(i).select("td");
                String handle = tds.get(1).text().split(" ")[0];
                if(handle.equals(contestRecord.getAccount())) {
                    char index = 'A';
                    for (int j = 4; j < 4 + contest.getProblemNumber(); ++j) {
                        String[] statusStrings = tds.get(j).text().split(" ");
                        String status = "UnSolved";
                        int tries = 0;
                        int penalty = 0;
                        if (tds.get(j).text().length() == 0) ;
                        else if(statusStrings.length == 1) {
                            log.info(statusStrings[0]);
                            if(statusStrings[0].contains("-")) {
                                tries = Integer.parseInt(statusStrings[0].substring(2, statusStrings[0].length()-1))+1;
                            } else {
                                tries = 1;
                                penalty = Integer.parseInt(statusStrings[0].split(":")[0])*60 + Integer.parseInt(statusStrings[0].split(":")[1]);
                                status = "Solved";
                            }

                        } else {
                            log.info(statusStrings[0] + " " + statusStrings[1]);
                            penalty = Integer.parseInt(statusStrings[0].split(":")[0])*60 + Integer.parseInt(statusStrings[0].split(":")[1]);
                            tries = Integer.parseInt(statusStrings[1].substring(2, statusStrings[1].length()-1))+1;
                            status = "Solved";
                        }
                        ContestProblemRecord contestProblemRecord = contestProblemRecordRepository.findByContestRecordIdAndProblemIndex(contestRecord.getId(), String.valueOf(index)).orElse(new ContestProblemRecord());
                        contestProblemRecord.setProblemIndex(String.valueOf(index));
                        contestProblemRecord.setPenalty(penalty);
                        contestProblemRecord.setTries(tries);
                        contestProblemRecord.setContestRecordId(contestRecord.getId());
                        contestProblemRecord.setStatus(status);
                        contestProblemRecordRepository.save(contestProblemRecord);
                        ++index;
                        log.info(tds.get(j).text());
                    }
                    hasTakeParkIn = true;
                    break;
                }
            }
            if (hasTakeParkIn) {
                break;
            }
        }
    }
}
