package cn.edu.scau.acm.acmer.service;

import cn.edu.scau.acm.acmer.httpclient.BaseHttpClient;
import cn.edu.scau.acm.acmer.model.PersonalContestLine;

import java.util.List;

public interface ContestService {
    void addPersonalContestRecord(String ojName, String cId, String password, String studentId, String account) throws Exception;
    int addContest(BaseHttpClient httpClient, String ojName, String ojId, String username, String password) throws Exception;
    List<PersonalContestLine> getPersonalContestByStudentId(String studentId);
}
