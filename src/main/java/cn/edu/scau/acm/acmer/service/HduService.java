package cn.edu.scau.acm.acmer.service;

import cn.edu.scau.acm.acmer.entity.OjAccount;

public interface HduService {
    void hduLogout();
    boolean checkHduAccount(String username, String password);
    void getAcProblemsByHduAccount(OjAccount hduAccount);
    void getAllAcProblems();
}
