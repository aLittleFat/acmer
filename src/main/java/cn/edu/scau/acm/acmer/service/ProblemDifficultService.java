package cn.edu.scau.acm.acmer.service;

import java.math.BigDecimal;

public interface ProblemDifficultService {
    void updateProblemDifficult(Integer problemId, String studentId, BigDecimal difficult);
}
