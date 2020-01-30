package cn.edu.scau.acm.acmer.service;

import cn.edu.scau.acm.acmer.entity.OJAccount;

public interface BzojService {
    void bzojLogout();
    boolean checkBzojAccount(String username, String password);
    void getAcProblemsByBzojAccount(OJAccount bzojAccount);
    void getAllAcProblems();
}
