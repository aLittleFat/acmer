package cn.edu.scau.acm.acmer.service;

import cn.edu.scau.acm.acmer.entity.OjAccount;

public interface CfService {
    void getAcProblemsByCfAccount(OjAccount cfAccount);
    void getAllAcProblems();
    boolean checkCfAccount(String username, String password);

    void addContestRecord(String ojName, String cId, String studentId, Integer teamId, String account) throws Exception;

    void addContest(String ojName, String cId) throws Exception;
}
