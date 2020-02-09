package cn.edu.scau.acm.acmer.service;

import cn.edu.scau.acm.acmer.entity.OjAccount;
import cn.edu.scau.acm.acmer.httpclient.VjudgeClient;

public interface VjService {
    boolean checkVjLoginStatus();
    void vjLogout();
    boolean checkVjAccount(String username, String password);
    void getAcProblemsByVjAccount(OjAccount vjAccount);
    void getAllAcProblems();
    void login(VjudgeClient vjudgeClient) throws Exception;
    void addContest(String ojId, String password) throws Exception;
    void loginContest(VjudgeClient vjudgeClient, String cId, String password) throws Exception;
}
