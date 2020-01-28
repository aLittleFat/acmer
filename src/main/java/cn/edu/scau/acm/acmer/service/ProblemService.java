package cn.edu.scau.acm.acmer.service;

import cn.edu.scau.acm.acmer.entity.OJAccount;
import cn.edu.scau.acm.acmer.entity.Problem;

import java.util.Date;

public interface ProblemService {
    void addProblem(String ojName, String problemId);
    boolean addProblemAcRecord(Problem problem, OJAccount ojAccount, Long time);
    Problem findProblem(String ojName, String problemId);
}
