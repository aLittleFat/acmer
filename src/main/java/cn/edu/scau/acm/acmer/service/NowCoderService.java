package cn.edu.scau.acm.acmer.service;

import cn.edu.scau.acm.acmer.entity.Contest;
import cn.edu.scau.acm.acmer.entity.ContestRecord;
import cn.edu.scau.acm.acmer.httpclient.BaseHttpClient;

public interface NowCoderService {
    void addContestRecord(String ojName, String cId, String studentId, Integer teamId, String account) throws Exception;
    void addContest(String ojName, String cId) throws Exception;
    void updateContestProblemRecord(ContestRecord contestRecord) throws Exception;

    void updateContestProblem(Contest contest);
}
