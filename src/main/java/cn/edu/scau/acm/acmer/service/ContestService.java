package cn.edu.scau.acm.acmer.service;

import cn.edu.scau.acm.acmer.entity.ContestRecord;
import cn.edu.scau.acm.acmer.model.ContestTable;
import cn.edu.scau.acm.acmer.model.MyResponseEntity;
import com.alibaba.fastjson.JSONObject;

public interface ContestService {
    void addContestRecord(String ojName, String cId, String studentId, Integer teamId, String account, String password) throws Exception;
    void addContest(String ojName, String cId, String username, String password) throws Exception;
    JSONObject getContestTableByStudentId(String studentId);
    JSONObject getContestTableByTeamId(Integer teamId, String studentId);
    ContestTable getContestTableByContestId(Integer contestId) throws Exception;
    void updateUpSolved(ContestRecord contestRecord);

    void changeSolution(Integer contestRecordId, String solution) throws Exception;

    JSONObject getContestInfo(Integer contestId);

    void deleteContestRecord(Integer contestRecordId, String studentId) throws Exception;
}
