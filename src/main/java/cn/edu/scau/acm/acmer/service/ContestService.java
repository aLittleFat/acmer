package cn.edu.scau.acm.acmer.service;

import cn.edu.scau.acm.acmer.httpclient.BaseHttpClient;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

public interface ContestService {
    void addPersonalContestRecord(String ojName, String cId, String password, String studentId, String account) throws Exception;
    int addContest(BaseHttpClient httpClient, String ojName, String ojId, String username, String password) throws Exception;
    List<JSONObject> getContestByStudentId(String studentId);
}
