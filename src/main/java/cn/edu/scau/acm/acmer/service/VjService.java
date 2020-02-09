package cn.edu.scau.acm.acmer.service;

import cn.edu.scau.acm.acmer.entity.OjAccount;
import cn.edu.scau.acm.acmer.httpclient.VjudgeClient;
import com.alibaba.fastjson.JSONObject;
import org.springframework.http.HttpEntity;

public interface VjService {
    boolean checkVjLoginStatus();
    void vjLogout();
    boolean checkVjAccount(String username, String password);
    void getAcProblemsByVjAccount(OjAccount vjAccount);
    void getAllAcProblems();
    void login(VjudgeClient vjudgeClient) throws Exception;
    String addContest(String ojId, String password) throws Exception;
}
