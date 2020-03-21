package cn.edu.scau.acm.acmer.service;

import cn.edu.scau.acm.acmer.entity.Contest;
import cn.edu.scau.acm.acmer.entity.OjAccount;
import cn.edu.scau.acm.acmer.entity.ContestRecord;
import cn.edu.scau.acm.acmer.httpclient.BaseHttpClient;

public interface VjService {
    boolean checkVjLoginStatus();
    void vjLogout();
    boolean checkVjAccount(String username, String password);
    void getAcProblemsByVjAccount(OjAccount vjAccount) throws Exception;
    void getAllAcProblems();
    void login(BaseHttpClient baseHttpClient) throws Exception;
    void addContest(String ojName, String cId, String password) throws Exception;
    void updateContestProblem(BaseHttpClient httpClient, Contest contest) throws Exception;
    void loginContest(BaseHttpClient baseHttpClient, String cId, String password) throws Exception;
    void addContestRecord(String ojName, String cId, String studentId, Integer teamId, String account, String password) throws Exception;
    void updateContest(BaseHttpClient httpClient, Contest contest) throws Exception;
}
