package cn.edu.scau.acm.acmer.service;

import cn.edu.scau.acm.acmer.entity.OjAccount;
import cn.edu.scau.acm.acmer.entity.Problem;
import cn.edu.scau.acm.acmer.model.AcProblemInDay;
import cn.edu.scau.acm.acmer.model.MyResponseEntity;
import cn.edu.scau.acm.acmer.model.PersonalProblemAcRank;

import java.util.Date;
import java.util.List;

public interface ProblemService {
    void addProblem(String ojName, String problemId);
    boolean addProblemAcRecord(Problem problem, OjAccount ojAccount, Long time);
    Problem findProblem(String ojName, String problemId);
    AcProblemInDay getProblemAcRecordInDay(List<OjAccount> ojAccounts, List<OjAccount> exOjAccounts, Date startTime, Date endTime);
    MyResponseEntity<List<AcProblemInDay>> getProblemAcRecordSeveralDays(String studentId, Date time, int days, String exStudentId);
    void getAllAcProblemsFromOj();
    MyResponseEntity<List<PersonalProblemAcRank>> getPersonalProblemAcRank(int grade, boolean includeRetired);
}
