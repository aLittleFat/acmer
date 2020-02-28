package cn.edu.scau.acm.acmer.service;

import com.alibaba.fastjson.JSONObject;

public interface ContestService {
    void addContestRecord(String ojName, String cId, String studentId, Integer teamId, String account, String password) throws Exception;
//    int addContest(BaseHttpClient httpClient, String ojName, String ojId, String username, String password) throws Exception;
    JSONObject getContestByStudentId(String studentId);
}
