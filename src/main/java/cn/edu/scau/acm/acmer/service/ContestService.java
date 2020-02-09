package cn.edu.scau.acm.acmer.service;

public interface ContestService {
    String addContestRecord(String ojName, String ojId, String password, String studentId, String account);
    String addContest(String ojName, String ojId, String password) throws Exception;
}
