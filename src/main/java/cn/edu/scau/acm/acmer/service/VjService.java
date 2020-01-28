package cn.edu.scau.acm.acmer.service;

import cn.edu.scau.acm.acmer.entity.OJAccount;

public interface VjService {
    boolean checkVjLoginStatus();
    void vjLogout();
    boolean checkVjAccount(String username, String password);
    void getAcProblemsByVjAccount(OJAccount vjAccount);
    void getAllAcProblems();
}
