package cn.edu.scau.acm.acmer.service.impl;

import cn.edu.scau.acm.acmer.entity.*;
import cn.edu.scau.acm.acmer.httpclient.BaseHttpClient;
import cn.edu.scau.acm.acmer.repository.*;
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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    private ContestRecordRepository contestRecordRepository;

    @Autowired
    private ContestProblemRecordRepository contestProblemRecordRepository;

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
    @Async
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
    @Async
    public void getAllAcProblems() {
        List<OjAccount> ojAccounts = ojAccountRepository.findAllByOjName("VJ");
        for(OjAccount ojAccount : ojAccounts) {
            getAcProblemsByVjAccount(ojAccount);
        }
    }

    @Override
    public void login(BaseHttpClient httpClient) throws Exception {
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("username", adminUsername));
        params.add(new BasicNameValuePair("password", adminPassword));
        String result = httpClient.post("https://vjudge.net/user/login" , params);
        if(!result.equals("success")) {
            throw new Exception("登录VJ失败");
        }
    }

    @Override
    public void addContest(BaseHttpClient httpClient, String cId, String password) throws Exception {
        httpClient = new BaseHttpClient();
        login(httpClient);
        String url = "https://vjudge.net/contest/" + cId;
        try {
            String html = httpClient.get(url);
            Document document = Jsoup.parse(html);
            Element element = document.body().selectFirst("[name=dataJson]");
            JSONObject jsonObject = (JSONObject) JSON.parse(element.text());
            if(jsonObject.getString("authStatus").equals("0")) {
                if(password.equals(""))
                    throw new Exception("需要密码");
                else {
                    loginContest(httpClient, cId, password);
                }
            }
            html = httpClient.get(url);
            document = Jsoup.parse(html);
            jsonObject = (JSONObject) JSON.parse(document.body().selectFirst("[name=dataJson]").text());
            Contest contest = new Contest();
            contest.setTitle(jsonObject.getString("title"));
            contest.setOjName("VJ");
            contest.setCid(cId);
            Timestamp startTime = new Timestamp(jsonObject.getLong("begin") + 8*60*60*1000);
            Timestamp endTime = new Timestamp(jsonObject.getLong("end") + 8*60*60*1000);
            contest.setStartTime(startTime);
            contest.setEndTime(endTime);
            contestRepository.save(contest);
            contest = contestRepository.findByOjNameAndCid("VJ", cId).get();
            if (System.currentTimeMillis() > endTime.getTime()) {
                updateContestProblem(httpClient, contest);
            }
        } catch (ProtocolException e) {
            throw new Exception("比赛不存在");
        }

    }

    @Override
    public void updateContestProblem(BaseHttpClient httpClient, Contest contest) throws Exception {
        if (httpClient == null) {
            httpClient = new BaseHttpClient();
            login(httpClient);
        }
        String url = "https://vjudge.net/contest/" + contest.getCid();
        String html = httpClient.get(url);
        Document document = Jsoup.parse(html);
        JSONObject jsonObject = (JSONObject) JSON.parse(document.body().selectFirst("[name=dataJson]").text());
        JSONArray jsonProblems = jsonObject.getJSONArray("problems");
        contest.setProblemNumber(jsonProblems.size());
        for(Object jsonProblemObject : jsonProblems) {
            JSONObject jsonProblem = (JSONObject) jsonProblemObject;
            log.info(jsonProblem.toJSONString());
            problemService.addProblem(jsonProblem.getString("oj"), jsonProblem.getString("probNum"));
            Problem problem = problemService.findProblem(jsonProblem.getString("oj"), jsonProblem.getString("probNum"));
            Optional<ContestProblem> contestProblem = contestProblemRepository.findByContestIdAndIndex(contest.getId(), jsonProblem.getString("num"));
            if(contestProblem.isPresent()) continue;
            ContestProblem contestProblem1 = new ContestProblem();
            contestProblem1.setContestId(contest.getId());
            contestProblem1.setIndex(jsonProblem.getString("num"));
            contestProblem1.setProblemId(problem.getId());
            contestProblemRepository.save(contestProblem1);
        }
    }

    @Override
    public void loginContest(BaseHttpClient httpClient, String cId, String password) throws Exception {
        try {
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("password", password));
            String result = httpClient.post("https://vjudge.net/contest/login/" + cId, params);
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

    @Override
    public void addPersonalContestRecord(BaseHttpClient httpClient, int contestId, String studentId, String account) throws Exception {
        if(httpClient == null) {
            httpClient = new BaseHttpClient();
            login(httpClient);
        }
        Contest contest = contestRepository.findById(contestId).get();

        ContestRecord contestRecord = new ContestRecord();
        contestRecord.setStudentId(studentId);
        contestRecord.setContestId(contestId);
        contestRecord.setAccount(account);
        contestRecord.setTime(contest.getStartTime());
        contestRecordRepository.save(contestRecord);


        contestRecord = contestRecordRepository.findByContestIdAndStudentId(contestId, studentId).get();

        log.info(contest.getEndTime().getTime() + " " + System.currentTimeMillis());

        if(contest.getEndTime().getTime() < System.currentTimeMillis()) {
            updatePersonalContestProblemRecord(httpClient, contestRecord);
        }
    }

    @Override
    @Async
    public void updatePersonalContestProblemRecord(BaseHttpClient httpClient, ContestRecord contestRecord) throws Exception {
        if(httpClient == null) {
            httpClient = new BaseHttpClient();
            login(httpClient);
        }

        Contest contest = contestRepository.findById(contestRecord.getContestId()).get();
        String url = "https://vjudge.net/contest/rank/single/" + contest.getCid();
        JSONObject jsonObject = JSON.parseObject(httpClient.get(url));
        int participantId = 0;
        JSONObject participants = jsonObject.getJSONObject("participants");
        for(String key : participants.keySet()) {
            if(participants.getJSONArray(key).get(0).toString().equals(contestRecord.getAccount())) {
                participantId = Integer.parseInt(key);
                break;
            }
        }

        List<ContestProblem> contestProblems = contestProblemRepository.findAllByContestIdOrderByIndexAsc(contest.getId());
        List<ContestProblemRecord> contestProblemRecords = new ArrayList<>();
        for (ContestProblem contestProblem : contestProblems) {
            ContestProblemRecord contestProblemRecord = contestProblemRecordRepository.findByContestRecordIdAndContestProblemId(contestRecord.getId(), contestProblem.getId()).orElse(new ContestProblemRecord());
            contestProblemRecord.setStatus("UnSolved");
            contestProblemRecord.setContestProblemId(contestProblem.getId());
            contestProblemRecord.setTries(0);
            contestProblemRecord.setContestRecordId(contestRecord.getId());
            contestProblemRecords.add(contestProblemRecord);
        }

        int contestLength = (int) ((contest.getEndTime().getTime() - contest.getStartTime().getTime()) / (1000 * 60));
        log.info(String.valueOf(contestLength));

        for (Object submission : jsonObject.getJSONArray("submissions")) {
            JSONArray jsonSubmission = (JSONArray) submission;
            if(jsonSubmission.getInteger(0) == participantId) {
                log.info(jsonSubmission.toJSONString());
                int proNum = jsonSubmission.getInteger(1);
                int isAc = jsonSubmission.getInteger(2);
                int time = jsonSubmission.getInteger(3) / 60;
                if(!contestProblemRecords.get(proNum).getStatus().equals("UnSolved")) continue;
                contestProblemRecords.get(proNum).setTries(contestProblemRecords.get(proNum).getTries() + 1);
                if (isAc != 0) {
                    if (time > contestLength) {
                        contestProblemRecords.get(proNum).setStatus("UpSolved");
                    } else {
                        contestProblemRecords.get(proNum).setStatus("Solved");
                    }
                    contestProblemRecords.get(proNum).setPenalty(time);
                }
            }
        }
        contestProblemRecordRepository.saveAll(contestProblemRecords);
    }

    @Override
    @Async
    public void updateContest(BaseHttpClient httpClient, Contest contest) throws Exception {
        if(httpClient == null) {
            httpClient = new BaseHttpClient();
            login(httpClient);
        }
        if(contest.getEndTime().getTime() > System.currentTimeMillis()) return;
        updateContestProblem(httpClient, contest);
        List<ContestRecord> contestRecords = contestRecordRepository.findAllByContestId(contest.getId());
        for (ContestRecord contestRecord : contestRecords) {
            updatePersonalContestProblemRecord(httpClient, contestRecord);
        }
    }

    @Override
    @Async
    public void updateAllContest(BaseHttpClient httpClient) throws Exception {
        List<Contest> contests = contestRepository.findAllByOjName("VJ");
        for(Contest contest : contests) {
            updateContest(httpClient, contest);
        }
    }
}
