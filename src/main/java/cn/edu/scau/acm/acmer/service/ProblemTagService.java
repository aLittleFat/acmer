package cn.edu.scau.acm.acmer.service;

public interface ProblemTagService {
    void updateProblemTag(Integer problemId, String studentId, String tagName) throws Exception;

    void deleteProblemTag(Integer problemId, String studentId, String tagName) throws Exception;
}
