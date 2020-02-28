package cn.edu.scau.acm.acmer.service;

import cn.edu.scau.acm.acmer.entity.ContestRecord;
import cn.edu.scau.acm.acmer.httpclient.BaseHttpClient;

public interface OpenTrainsService {
    String login(String username) throws Exception;
    void addContest(String ojName, String cId, String username) throws Exception;
    void addContestRecord(String ojName, String cId, String studentId, Integer teamId, String account) throws Exception;
    void updateContestProblemRecord(ContestRecord contestRecord) throws Exception;
}
