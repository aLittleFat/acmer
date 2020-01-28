package cn.edu.scau.acm.acmer.service.impl;

import cn.edu.scau.acm.acmer.entity.OJAccount;
import cn.edu.scau.acm.acmer.repository.OJAccountRepository;
import cn.edu.scau.acm.acmer.service.OJService;
import cn.edu.scau.acm.acmer.service.ProblemService;
import cn.edu.scau.acm.acmer.service.VjService;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
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

import java.util.List;

@Service
public class VjServiceImpl implements VjService {

    Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    ProblemService problemService;

    @Autowired
    OJAccountRepository ojAccountRepository;

    @Autowired
    OJService ojService;

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
    public void getAcProblemsByVjAccount(OJAccount vjAccount) {
        String url = "https://vjudge.net/status/data/";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        int sz = 0;
        int start = 0;
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
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
            JSONObject jsonObject = JSONObject.parseObject(response);
            JSONArray vjProblems = jsonObject.getJSONArray("data");
            sz = vjProblems.size();
            for (Object vjProblem : vjProblems) {
                JSONObject jsonProblem = (JSONObject) vjProblem;
                ojService.addOj(jsonProblem.getString("oj"));
                problemService.addProblem(jsonProblem.getString("oj"),jsonProblem.getString("probNum"));
                if(!problemService.addProblemAcRecord(problemService.findProblem(jsonProblem.getString("oj"),jsonProblem.getString("probNum")), vjAccount, jsonProblem.getLong("time"))){
                    return;
                }
            }
            start += 20;
            log.info(String.valueOf(sz));
        } while (sz >= 20);
    }

    @Override
    @Scheduled(cron = "0 0 2 * * ?")
    public void getAllAcProblems() {
        List<OJAccount> ojAccounts = ojAccountRepository.findAllByOjName("VJ");
        for(OJAccount ojAccount : ojAccounts) {
            getAcProblemsByVjAccount(ojAccount);
        }
    }
}
