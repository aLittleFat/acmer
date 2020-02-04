package cn.edu.scau.acm.acmer.service.impl;

import cn.edu.scau.acm.acmer.entity.OJAccount;
import cn.edu.scau.acm.acmer.entity.Problem;
import cn.edu.scau.acm.acmer.repository.OJAccountRepository;
import cn.edu.scau.acm.acmer.service.HduService;
import cn.edu.scau.acm.acmer.service.ProblemService;
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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.util.List;

@Service
public class HduServiceImpl implements HduService {

    Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    OJAccountRepository ojAccountRepository;

    @Autowired
    ProblemService problemService;

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
    public void getAcProblemsByHduAccount(OJAccount hduAccount) {
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
    @Async
    public void getAllAcProblems() {
        List<OJAccount> ojAccounts = ojAccountRepository.findAllByOjName("HDU");
        for(OJAccount ojAccount : ojAccounts) {
            getAcProblemsByHduAccount(ojAccount);
        }
    }
}
