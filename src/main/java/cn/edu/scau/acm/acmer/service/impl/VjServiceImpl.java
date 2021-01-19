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
import org.apache.commons.lang3.StringUtils;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.util.*;

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
    private ContestRepository contestRepository;

    @Autowired
    private ContestProblemRepository contestProblemRepository;

    @Autowired
    private ContestRecordRepository contestRecordRepository;

    @Autowired
    private ProblemRepository problemRepository;

    @Autowired
    private TeamStudentRepository teamStudentRepository;

    @Override
    public boolean checkVjLoginStatus() {
        String url = "https://vjudge.net/user/checkLogInStatus";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        String res = restTemplate.postForObject(url, request, String.class);
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
        return response.equals("success");
    }

    @Override
    @Async
    @Transactional
    public void getAcProblemsByVjAccount(OjAccount vjAccount) throws Exception {
        BaseHttpClient httpClient = new BaseHttpClient();
        String url = "https://vjudge.net/status/data/";
        int sz = 0;
        int start = 0;
        do {
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("start", String.valueOf(start)));
            params.add(new BasicNameValuePair("length", "20"));
            params.add(new BasicNameValuePair("un", vjAccount.getAccount()));
            params.add(new BasicNameValuePair("OJId", "All"));
            params.add(new BasicNameValuePair("res", "1"));
            String response = httpClient.post(url, params);
            JSONObject jsonObject = JSONObject.parseObject(response);
            JSONArray vjProblems = jsonObject.getJSONArray("data");
            sz = vjProblems.size();
            for (Object vjProblem : vjProblems) {
                JSONObject jsonProblem = (JSONObject) vjProblem;
                String ojName = jsonProblem.getString("oj");
                String problemId = jsonProblem.getString("probNum");

                if(problemRepository.findByOjNameAndProblemId(ojName,problemId).isEmpty()) {
                    response = httpClient.get("https://vjudge.net/problem/" + ojName + "-" + problemId);
                    String title = Jsoup.parse(response).selectFirst("h2").text();
                    problemService.addProblem(ojName, problemId, title);
                }
                if(problemService.addProblemAcRecord(problemService.findProblem(ojName, problemId), vjAccount, jsonProblem.getLong("time")+8*60*60*1000)){
                    break;
                }
            }
            start += 20;
        } while (sz >= 20);
    }

    @Override
    @Async
    public void getAllAcProblems() {
        List<OjAccount> ojAccounts = ojAccountRepository.findAllByOjName("VJ");
        for(OjAccount ojAccount : ojAccounts) {
            try {
                getAcProblemsByVjAccount(ojAccount);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
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
    @Transactional
    public void addContest(String ojName, String cId, String password) throws Exception {

        BaseHttpClient httpClient = new BaseHttpClient();
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
            JSONArray jsonProblems = jsonObject.getJSONArray("problems");
            Contest contest = new Contest();
            if(jsonProblems != null) {
                List<String> problemList = new ArrayList<>();
                for (char i = 'A', j = 0; j < jsonProblems.size(); j++, i++) {
                    problemList.add(String.valueOf(i));
                }
                contest.setProblemList(StringUtils.join(problemList, " "));
            }
            else {
                contest.setProblemList("");
            }
            contest.setTitle(jsonObject.getString("title"));
            contest.setOjName(ojName);
            contest.setCid(cId);

            Timestamp startTime = new Timestamp(jsonObject.getLong("begin") + 8*60*60*1000);
            Timestamp endTime = new Timestamp(jsonObject.getLong("end") + 8*60*60*1000);
            contest.setStartTime(startTime);
            contest.setEndTime(endTime);
            contestRepository.save(contest);
            contest = contestRepository.findByOjNameAndCid("VJ", cId).get();
            if (!contest.getProblemList().equals("")) {
                updateContestProblem(httpClient, contest);
            }
        } catch (ProtocolException e) {
            throw new Exception("比赛不存在");
        }
    }

    @Override
    @Async
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
        List<String> problemList = new ArrayList<>();
        for (char i = 'A', j = 0; j < jsonProblems.size(); j++, i++) {
            problemList.add(String.valueOf(i));
        }
        contest.setProblemList(StringUtils.join(problemList, " "));
        contestRepository.save(contest);
        for(Object jsonProblemObject : jsonProblems) {
            JSONObject jsonProblem = (JSONObject) jsonProblemObject;

            String ojName = jsonProblem.getString("oj");
            String problemId = jsonProblem.getString("probNum");
            if(problemRepository.findByOjNameAndProblemId(ojName, problemId).isEmpty()) {
                problemService.addProblem(ojName, problemId, Jsoup.parse(httpClient.get("https://vjudge.net/problem/" + ojName + "-" + problemId)).selectFirst("h2").text());
            }
            Problem problem = problemService.findProblem(ojName, problemId);
            Optional<ContestProblem> optionalContestProblem = contestProblemRepository.findByContestIdAndProblemIndex(contest.getId(), jsonProblem.getString("num"));
            if(optionalContestProblem.isPresent()) continue;
            ContestProblem contestProblem = new ContestProblem();
            contestProblem.setContestId(contest.getId());
            contestProblem.setProblemIndex(jsonProblem.getString("num"));
            contestProblem.setProblemId(problem.getId());
            contestProblemRepository.save(contestProblem);
        }
    }

    @Override
    public void loginContest(BaseHttpClient httpClient, String cId, String password) throws Exception {
        try {
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("password", password));
            String result = httpClient.post("https://vjudge.net/contest/login/" + cId, params);
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
    public void addContestRecord(String ojName, String cId, String studentId, Integer teamId, String account, String password) throws Exception {

        BaseHttpClient httpClient = new BaseHttpClient();
        login(httpClient);

        Contest contest = contestRepository.findByOjNameAndCid(ojName, cId).get();
        Optional<ContestRecord> optionalContestRecord = contestRecordRepository.findByContestIdAndStudentIdAndTeamId(contest.getId(), studentId, teamId);

        if (optionalContestRecord.isPresent()) {
            throw new Exception("已存在该竞赛记录");
        }

        String url = "https://vjudge.net/contest/rank/single/" + contest.getCid();
        JSONObject jsonObject = JSON.parseObject(httpClient.get(url));
        int participantId = 0;
        JSONObject participants = jsonObject.getJSONObject("participants");
        for(String key : participants.keySet()) {
            if(participants.getJSONArray(key).get(0).toString().equals(account)) {
                participantId = Integer.parseInt(key);
                break;
            }
        }
        if(participantId == 0) {
            throw new Exception("没有参加该比赛");
        }

        ContestRecord contestRecord = new ContestRecord();
        contestRecord.setStudentId(studentId);
        contestRecord.setTeamId(teamId);
        contestRecord.setContestId(contest.getId());
        contestRecord.setAccount(account);
        contestRecord.setTime(contest.getStartTime());

        int contestLength = (int) ((contest.getEndTime().getTime() - contest.getStartTime().getTime()) / 1000);

        TreeSet<String> solved = new TreeSet<>();
        TreeSet<String> upSolved = new TreeSet<>();

        int penalty = 0;

        List<String> problemList = Arrays.asList(contest.getProblemList().split(" "));

        List<Integer> penaltys = new ArrayList<>();
        for (int i = 0; i < problemList.size(); i++) {
            penaltys.add(0);
        }

        for (Object submission : jsonObject.getJSONArray("submissions")) {
            JSONArray jsonSubmission = (JSONArray) submission;
            if(jsonSubmission.getInteger(0) == participantId) {
                int proNum = jsonSubmission.getInteger(1);
                int isAc = jsonSubmission.getInteger(2);
                int time = jsonSubmission.getInteger(3);

                String index = problemList.get(proNum);

                if(isAc == 1) {
                    if(time > contestLength) {
                        if(!solved.contains(index)) {
                            upSolved.add(index);
                        }
                    } else {
                        if(!solved.contains(String.valueOf(index))){
                            solved.add(String.valueOf(index));
                            penalty += penaltys.get(proNum) + time;
                        }
                    }
                } else {
                    if(!solved.contains(String.valueOf(index))){
                        penaltys.set(proNum, penaltys.get(proNum) + 1200);
                    }
                }
            }
        }
        contestRecord.setPenalty(penalty);
        contestRecord.setSolved(StringUtils.join(solved.toArray(), " "));
        contestRecord.setUpSolved(StringUtils.join(upSolved.toArray(), " "));
        contestRecordRepository.save(contestRecord);

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
            updateContestRecord(httpClient, contestRecord);
        }
    }

    private void updateContestRecord(BaseHttpClient httpClient, ContestRecord contestRecord) throws Exception {
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

        int contestLength = (int) ((contest.getEndTime().getTime() - contest.getStartTime().getTime()) / 1000);

        TreeSet<String> upSolved = new TreeSet<>();
        TreeSet<String> solved = new TreeSet<>(Arrays.asList(contestRecord.getSolved().split(" ")));


        List<String> problemList = Arrays.asList(contest.getProblemList().split(" "));

        for (Object submission : jsonObject.getJSONArray("submissions")) {
            JSONArray jsonSubmission = (JSONArray) submission;
            if(jsonSubmission.getInteger(0) == participantId) {
                int proNum = jsonSubmission.getInteger(1);
                int isAc = jsonSubmission.getInteger(2);
                int time = jsonSubmission.getInteger(3);

                String index = problemList.get(proNum);

                if(isAc == 1) {
                    if (time > contestLength) {
                        if (!solved.contains(index)) {
                            upSolved.add(index);
                        }
                    }
                }
            }
        }

        contestRecord.setUpSolved(StringUtils.join(upSolved.toArray(), " "));
        contestRecordRepository.save(contestRecord);
    }
}
