package cn.edu.scau.acm.acmer.service;

import com.sun.istack.Nullable;

public interface ContestService {
    String addContestRecord(String ojName, String ojId, String password, String studentId, String account);
    void addContest(String ojName, String ojId, String username, String password) throws Exception;
    void addContestProblem(int contestId, String idInContest, @Nullable Integer problemId);
}
