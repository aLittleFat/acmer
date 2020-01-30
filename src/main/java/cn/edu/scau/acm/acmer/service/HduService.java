package cn.edu.scau.acm.acmer.service;

import cn.edu.scau.acm.acmer.entity.OJAccount;

public interface HduService {
    void hduLogout();
    boolean checkHduAccount(String username, String password);
    void getAcProblemsByHduAccount(OJAccount hduAccount);
    void getAllAcProblems();
}
