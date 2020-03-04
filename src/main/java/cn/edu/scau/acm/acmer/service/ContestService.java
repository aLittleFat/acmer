package cn.edu.scau.acm.acmer.service;

import com.alibaba.fastjson.JSONObject;

public interface ContestService {
    void addContestRecord(String ojName, String cId, String studentId, Integer teamId, String account, String password) throws Exception;
    void addContest(String ojName, String cId, String username, String password) throws Exception;
    JSONObject getContestByStudentId(String studentId);
    JSONObject getContestByTeamId(Integer teamId);
}
