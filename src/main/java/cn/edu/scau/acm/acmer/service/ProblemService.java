package cn.edu.scau.acm.acmer.service;

import cn.edu.scau.acm.acmer.entity.OJAccount;
import cn.edu.scau.acm.acmer.entity.Problem;
import cn.edu.scau.acm.acmer.model.AcProblemInDay;

import java.util.Date;
import java.util.List;

public interface ProblemService {
    void addProblem(String ojName, String problemId);
    boolean addProblemAcRecord(Problem problem, OJAccount ojAccount, Long time);
    Problem findProblem(String ojName, String problemId);

    AcProblemInDay getProblemAcRecordInDay(List<OJAccount> ojAccounts, List<OJAccount> exOjAccounts, Date startTime, Date endTime);

    List<AcProblemInDay> getProblemAcRecordSeveralDays(String studentId, Date time, int days, String exStudentId);

    void getAllAcProblemsFromOj();



}
