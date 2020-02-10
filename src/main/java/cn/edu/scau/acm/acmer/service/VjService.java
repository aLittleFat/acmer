package cn.edu.scau.acm.acmer.service;

import cn.edu.scau.acm.acmer.entity.OjAccount;
import cn.edu.scau.acm.acmer.httpclient.BaseHttpClient;

public interface VjService {
    boolean checkVjLoginStatus();
    void vjLogout();
    boolean checkVjAccount(String username, String password);
    void getAcProblemsByVjAccount(OjAccount vjAccount);
    void getAllAcProblems();
    void login(BaseHttpClient baseHttpClient) throws Exception;
    void addContest(BaseHttpClient httpClient, String cId, String password) throws Exception;
    void loginContest(BaseHttpClient baseHttpClient, String cId, String password) throws Exception;
    void addPersonalContestRecord(BaseHttpClient httpClient, int contestId, String studentId, String account) throws Exception;
}
