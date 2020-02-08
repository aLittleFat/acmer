package cn.edu.scau.acm.acmer.service;

import cn.edu.scau.acm.acmer.entity.OjAccount;
import com.alibaba.fastjson.JSONObject;

public interface VjService {
    boolean checkVjLoginStatus();
    void vjLogout();
    boolean checkVjAccount(String username, String password);
    void getAcProblemsByVjAccount(OjAccount vjAccount);
    void getAllAcProblems();
}
