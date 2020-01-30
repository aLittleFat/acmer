package cn.edu.scau.acm.acmer.service;

import cn.edu.scau.acm.acmer.entity.OJAccount;
import cn.edu.scau.acm.acmer.entity.Problem;
import cn.edu.scau.acm.acmer.entity.ProblemACRecord;

import java.util.Date;
import java.util.List;

public interface ProblemService {
    void addProblem(String ojName, String problemId);
    boolean addProblemAcRecord(Problem problem, OJAccount ojAccount, Long time);
    Problem findProblem(String ojName, String problemId);
}
