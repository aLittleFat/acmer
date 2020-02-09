package cn.edu.scau.acm.acmer.service.impl;

import cn.edu.scau.acm.acmer.entity.Contest;
import cn.edu.scau.acm.acmer.entity.ContestProblem;
import cn.edu.scau.acm.acmer.entity.OjAccount;
import cn.edu.scau.acm.acmer.entity.Problem;
import cn.edu.scau.acm.acmer.httpclient.VjudgeClient;
import cn.edu.scau.acm.acmer.repository.ContestProblemRepository;
import cn.edu.scau.acm.acmer.repository.ContestRepository;
import cn.edu.scau.acm.acmer.repository.OjAccountRepository;
import cn.edu.scau.acm.acmer.service.ContestService;
import cn.edu.scau.acm.acmer.service.OJService;
import cn.edu.scau.acm.acmer.service.ProblemService;
import cn.edu.scau.acm.acmer.service.VjService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.NameValuePair;
import org.apache.http.ProtocolException;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.util.ArrayList;
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

    @Autowired
    private ContestRepository contestRepository;

    @Autowired
    private ContestProblemRepository contestProblemRepository;

    @Autowired
    private ContestService contestService;

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
    public void addContest(String ojId, String password) throws Exception {
        VjudgeClient vjudgeClient = new VjudgeClient();
        login(vjudgeClient);
        String url = "https://vjudge.net/contest/" + ojId;
        try {
            String html = vjudgeClient.get(url);
            Document document = Jsoup.parse(html);
            Element element = document.body().selectFirst("[name=dataJson]");
            JSONObject jsonObject = (JSONObject) JSON.parse(element.text());
            if(jsonObject.getString("authStatus").equals("0")) {
                if(password.equals(""))
                    throw new Exception("需要密码");
                else {
                    loginContest(vjudgeClient, ojId, password);
                }
            }
            html = vjudgeClient.get(url);
            document = Jsoup.parse(html);
            jsonObject = (JSONObject) JSON.parse(document.body().selectFirst("[name=dataJson]").text());
            log.info(jsonObject.toJSONString());
            Contest contest = new Contest();
            contest.setName(jsonObject.getString("title"));
            contest.setOjName("VJ");
            contest.setOjid(ojId);
            Timestamp startTime = new Timestamp(jsonObject.getLong("begin") + 8*60*60*1000);
            Timestamp endTime = new Timestamp(jsonObject.getLong("end") + 8*60*60*1000);
            contest.setStartTime(startTime);
            contest.setEndTime(endTime);
            contestRepository.save(contest);
            contest = contestRepository.findByOjNameAndOjid("VJ", ojId).get();

            JSONArray jsonProblems = jsonObject.getJSONArray("problems");
            for(Object jsonProblemObject : jsonProblems) {
                JSONObject jsonProblem = (JSONObject) jsonProblemObject;
                log.info(jsonProblem.toJSONString());
                problemService.addProblem(jsonProblem.getString("oj"), jsonProblem.getString("probNum"));
                Problem problem = problemService.findProblem(jsonProblem.getString("oj"), jsonProblem.getString("probNum"));
                contestService.addContestProblem(contest.getId(), jsonProblem.getString("num"), problem.getId());
            }
        } catch (ProtocolException e) {
            throw new Exception("比赛不存在");
        }

    }

    @Override
    public void loginContest(VjudgeClient vjudgeClient, String cId, String password) throws Exception {
        try {
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("password", password));
            String result = vjudgeClient.post("https://vjudge.net/contest/login/" + cId, params);
            log.info(result);
            log.info("{\"error\":\"Password is not correct!\"}");
            log.info(String.valueOf(result.equals("{\"error\":\"Password is not correct!\"}")));
            switch (result) {
                case "{}": {
                    return;
                }
                case "{\"error\":\"Password is not correct!\"}":
                    throw new Exception("密码错误");
                default:
                    throw new Exception("发生未知错误，请联系管理员");
            }
        } catch (UnknownHostException e) {
            throw new Exception("无法访问vjudge.net，请稍后再试");
        }
    }
}
