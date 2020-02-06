package cn.edu.scau.acm.acmer.service;

import cn.edu.scau.acm.acmer.entity.OJAccount;

public interface CfService {
    void getAcProblemsByCfAccount(OJAccount cfAccount);
    void getAllAcProblems();
    boolean checkCfAccount(String username, String password);
}
