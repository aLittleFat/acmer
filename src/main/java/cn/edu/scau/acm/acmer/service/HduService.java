package cn.edu.scau.acm.acmer.service;

import cn.edu.scau.acm.acmer.entity.OjAccount;
import cn.edu.scau.acm.acmer.httpclient.BaseHttpClient;

public interface HduService {
    void hduLogout();
    boolean checkHduAccount(String username, String password);
    void getAcProblemsByHduAccount(OjAccount hduAccount);
    void getAllAcProblems();

    void addContest(BaseHttpClient httpClient, String cId, String username, String password) throws Exception;

    void loginContest(BaseHttpClient httpClient, String ojId, String username, String password) throws Exception;

    void addPersonalContestRecord(BaseHttpClient httpClient, int contestId, String studentId, String account);
}
