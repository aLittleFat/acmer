package cn.edu.scau.acm.acmer.service;

import cn.edu.scau.acm.acmer.entity.Contest;
import cn.edu.scau.acm.acmer.entity.ContestRecord;
import cn.edu.scau.acm.acmer.entity.OjAccount;
import cn.edu.scau.acm.acmer.httpclient.BaseHttpClient;

public interface HduService {
    void hduLogout();
    boolean checkHduAccount(String username, String password);
    void getAcProblemsByHduAccount(OjAccount hduAccount);
    void getAllAcProblems();

    void addContest(String ojName, String cId, String username, String password) throws Exception;
    void updateContestProblem(BaseHttpClient httpClient, Contest contest) throws Exception;
    void loginContest(BaseHttpClient httpClient, String ojId, String username, String password) throws Exception;

    void addContestRecord(String ojName, String cId, String studentId, Integer teamId, String account, String password) throws Exception;

    void updateContestProblemRecord(BaseHttpClient httpClient, ContestRecord contestRecord) throws Exception;
}
