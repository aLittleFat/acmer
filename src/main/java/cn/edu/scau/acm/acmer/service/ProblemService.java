package cn.edu.scau.acm.acmer.service;

import cn.edu.scau.acm.acmer.entity.OjAccount;
import cn.edu.scau.acm.acmer.entity.Problem;
import cn.edu.scau.acm.acmer.model.AcProblemInDay;
import cn.edu.scau.acm.acmer.model.PersonalProblemAcRank;
import com.alibaba.fastjson.JSONObject;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface ProblemService {
    void addProblem(String ojName, String problemId, String title);
    boolean addProblemAcRecord(Problem problem, OjAccount ojAccount, Long time);
    Problem findProblem(String ojName, String problemId);

    AcProblemInDay getProblemAcRecordInDay(List<Integer> ojAccounts, List<Integer> exOjAccounts, Date startTime, Date endTime);

    List<AcProblemInDay> getProblemAcRecordSeveralDays(String studentId, Date time, int days, String exStudentId);
    List<PersonalProblemAcRank> getPersonalProblemAcRank(int grade, boolean includeRetired);

    JSONObject getProblemInfo(int id, String studentId);

    boolean checkIsAc(int problemId, String studentId);
}
