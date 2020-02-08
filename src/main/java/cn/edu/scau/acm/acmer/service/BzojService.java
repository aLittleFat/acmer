package cn.edu.scau.acm.acmer.service;

import cn.edu.scau.acm.acmer.entity.OjAccount;

public interface BzojService {
    void bzojLogout();
    boolean checkBzojAccount(String username, String password);
    void getAcProblemsByBzojAccount(OjAccount bzojAccount);
    void getAllAcProblems();
}
