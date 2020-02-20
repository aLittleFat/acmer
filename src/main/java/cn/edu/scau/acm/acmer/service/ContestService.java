package cn.edu.scau.acm.acmer.service;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

public interface ContestService {
    void addContestRecord(String ojName, String cId, String studentId, Integer teamId, String account, String password) throws Exception;
//    int addContest(BaseHttpClient httpClient, String ojName, String ojId, String username, String password) throws Exception;
    List<JSONObject> getContestByStudentId(String studentId);
}
