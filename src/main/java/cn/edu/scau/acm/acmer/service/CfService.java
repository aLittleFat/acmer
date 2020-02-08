package cn.edu.scau.acm.acmer.service;

import cn.edu.scau.acm.acmer.entity.OjAccount;

public interface CfService {
    void getAcProblemsByCfAccount(OjAccount cfAccount);
    void getAllAcProblems();
    boolean checkCfAccount(String username, String password);
}
