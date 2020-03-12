package cn.edu.scau.acm.acmer.service;

import cn.edu.scau.acm.acmer.entity.Contest;
import cn.edu.scau.acm.acmer.entity.ContestRecord;
import cn.edu.scau.acm.acmer.httpclient.BaseHttpClient;
import org.openqa.selenium.WebDriver;

public interface JisuankeService {
    void addContest(String ojName, String cId) throws Exception;

    void addContestRecord(String ojName, String cId, String studentId, Integer teamId, String account) throws Exception;

    void login(WebDriver webDriver) throws InterruptedException;
}
