package cn.edu.scau.acm.acmer.service;

import cn.edu.scau.acm.acmer.entity.OjAccount;
import cn.edu.scau.acm.acmer.httpclient.HduClient;
import org.apache.http.ProtocolException;

import java.io.IOException;

public interface HduService {
    void hduLogout();
    boolean checkHduAccount(String username, String password);
    void getAcProblemsByHduAccount(OjAccount hduAccount);
    void getAllAcProblems();

    void addContest(String ojId, String username, String password) throws Exception;

    void loginContest(HduClient hduClient, String ojId, String username, String password) throws Exception;
}
