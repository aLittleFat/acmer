package cn.edu.scau.acm.acmer.service.impl;

import cn.edu.scau.acm.acmer.entity.OjAccount;
import cn.edu.scau.acm.acmer.httpclient.VjudgeClient;
import cn.edu.scau.acm.acmer.repository.OjAccountRepository;
import cn.edu.scau.acm.acmer.service.OJService;
import cn.edu.scau.acm.acmer.service.ProblemService;
import cn.edu.scau.acm.acmer.service.VjService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;

@Service
public class VjServiceImpl implements VjService {

    Logger log = LoggerFactory.getLogger(this.getClass());

    @Value("${scau.acmer.ojaccounts.vj.username}")
    private String adminUsername;

    @Value("${scau.acmer.ojaccounts.vj.password}")
    private String adminPassword;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ProblemService problemService;

    @Autowired
    private OjAccountRepository ojAccountRepository;

    @Autowired
    private OJService ojService;

    @Override
    public boolean checkVjLoginStatus() {
        String url = "https://vjudge.net/user/checkLogInStatus";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        log.info(request.toString());
        String res = restTemplate.postForObject(url, request, String.class);
        log.info(res);
        return res.equals("true");
    }

    @Override
    public void vjLogout() {
        String url = "https://vjudge.net/user/logout";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        ResponseEntity<String> response = restTemplate.postForEntity( url, request , String.class );
    }

    @Override
    public boolean checkVjAccount(String username, String password) {
        if(checkVjLoginStatus()){
            vjLogout();
        }
        String url = "https://vjudge.net/user/login";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        map.add("username", username);
        map.add("password", password);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        String response = restTemplate.postForObject( url, request , String.class );
        log.info(response);
        return response.equals("success");
    }

    @Override
    public void getAcProblemsByVjAccount(OjAccount vjAccount) {
        String url = "https://vjudge.net/status/data/";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        int sz = 0;
        int start = 0;
        int retry = 10;
        do {
            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("start", String.valueOf(start));
            map.add("length", "20");
            map.add("un", vjAccount.getAccount());
            map.add("OJId", "All");
            map.add("res", "1");
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
            String response;
            try {
                response = restTemplate.postForObject(url, request, String.class);
                JSONObject jsonObject = JSONObject.parseObject(response);
                JSONArray vjProblems = jsonObject.getJSONArray("data");
                sz = vjProblems.size();
                for (Object vjProblem : vjProblems) {
                    JSONObject jsonProblem = (JSONObject) vjProblem;
                    log.info(jsonProblem.getString("oj") + "   " + jsonProblem.getString("probNum"));
                    ojService.addOj(jsonProblem.getString("oj"));
                    problemService.addProblem(jsonProblem.getString("oj"),jsonProblem.getString("probNum"));
                    if(!problemService.addProblemAcRecord(problemService.findProblem(jsonProblem.getString("oj"),jsonProblem.getString("probNum")), vjAccount, jsonProblem.getLong("time"))){
                        continue;
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
                continue;
            }
            start += 20;
            log.info(String.valueOf(sz));
        } while (sz >= 20 && retry > 0);
    }

    @Override
    public void getAllAcProblems() {
        List<OjAccount> ojAccounts = ojAccountRepository.findAllByOjName("VJ");
        for(OjAccount ojAccount : ojAccounts) {
            getAcProblemsByVjAccount(ojAccount);
        }
    }

    @Override
    public void login(VjudgeClient vjudgeClient) throws Exception {
        vjudgeClient.login(adminUsername, adminPassword);
    }

    @Override
    public String addContest(String ojId, String password) throws Exception {
        VjudgeClient vjudgeClient = new VjudgeClient();
        login(vjudgeClient);
        String url = "https://vjudge.net/contest/" + ojId;

        String html = vjudgeClient.get(url);
        Document document = Jsoup.parse(html);
        JSONObject jsonObject = (JSONObject) JSON.parse(document.body().selectFirst("[name=dataJson]").text());
        if(jsonObject.getString("authStatus").equals("0")) {
            if(password.equals(""))
                return "需要密码";
            else {

            }
        }
        log.info(jsonObject.toJSONString());
        return null;
    }
}
