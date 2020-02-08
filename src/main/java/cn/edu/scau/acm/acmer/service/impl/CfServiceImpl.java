package cn.edu.scau.acm.acmer.service.impl;

import cn.edu.scau.acm.acmer.entity.OjAccount;
import cn.edu.scau.acm.acmer.repository.OjAccountRepository;
import cn.edu.scau.acm.acmer.service.CfService;
import cn.edu.scau.acm.acmer.service.ProblemService;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class CfServiceImpl implements CfService {

    Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private OjAccountRepository ojAccountRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ProblemService problemService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void getAcProblemsByCfAccount(OjAccount cfAccount) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        int sz = 0;
        int start = 1;
        int retry = 20;
        do {
            String url = "https://codeforces.com/api/user.status?handle=" + cfAccount.getAccount() + "&from=" + start + "&count=20";
            String response;
            try {
                response = restTemplate.getForObject(url, String.class);
                JSONObject jsonObject = JSONObject.parseObject(response);
                JSONArray cfProblems = jsonObject.getJSONArray("result");
                sz = cfProblems.size();
                for (Object cfProblem : cfProblems) {
                    JSONObject jsonProblem = (JSONObject) cfProblem;
                    String proId = jsonProblem.getJSONObject("problem").getInteger("contestId") + jsonProblem.getJSONObject("problem").getString("index");
                    String ojName="CodeForces";
                    if(jsonProblem.getJSONObject("problem").getInteger("contestId") >= 9999) {
                        ojName="Gym";
                    }
                    problemService.addProblem(ojName,proId);
                    if(!problemService.addProblemAcRecord(problemService.findProblem(ojName, proId), cfAccount, jsonProblem.getLong("creationTimeSeconds")*1000)){
                        continue;
                    }
                }
                retry = 20;
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    Thread.sleep(2000);
                    retry--;
                    log.error("网络错误，准备重试，重试第{}次", 20-retry);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                continue;
            }
            start += 20;
            log.info(url + " " + String.valueOf(sz));
        } while (sz >= 20 && retry > 0);
    }

    @Override
    public void getAllAcProblems() {
        List<OjAccount> ojAccounts = ojAccountRepository.findAllByOjName("CodeForces");
        for(OjAccount ojAccount : ojAccounts) {
            getAcProblemsByCfAccount(ojAccount);
        }
    }

    @Override
    public boolean checkCfAccount(String username, String password) {
        String verifyCode = stringRedisTemplate.opsForValue().get(username + "_Verify");
        return verifyCode != null && verifyCode.equals(password);
    }
}
