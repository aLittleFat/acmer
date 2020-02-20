package cn.edu.scau.acm.acmer.service.impl;

import cn.edu.scau.acm.acmer.entity.OjAccount;
import cn.edu.scau.acm.acmer.entity.Problem;
import cn.edu.scau.acm.acmer.entity.ProblemAcRecord;
import cn.edu.scau.acm.acmer.entity.User;
import cn.edu.scau.acm.acmer.model.AcProblem;
import cn.edu.scau.acm.acmer.model.AcProblemInDay;
import cn.edu.scau.acm.acmer.model.PersonalProblemAcRank;
import cn.edu.scau.acm.acmer.repository.*;
import cn.edu.scau.acm.acmer.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ProblemServiceImpl implements ProblemService {

    Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ProblemRepository problemRepository;

    @Autowired
    private ProblemAcRecordRepository problemAcRecordRepository;

    @Autowired
    private OjAccountRepository ojAccountRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OJService ojService;

    @Override
    public void addProblem(String ojName, String problemId) {
        Optional<Problem> problem = problemRepository.findByOjNameAndProblemId(ojName, problemId);
        if(problem.isEmpty()) {
            ojService.addOj(ojName);
            Problem newProblem = new Problem();
            newProblem.setOjName(ojName);
            newProblem.setProblemId(problemId);
            problemRepository.save(newProblem);
        }
    }

    @Override
    public boolean addProblemAcRecord(Problem problem, OjAccount ojAccount, Long time) {
        Optional<ProblemAcRecord> problemACRecord = problemAcRecordRepository.findProblemACRecordByOjAccountIdAndProblemId(ojAccount.getId(), problem.getId());
        if(problemACRecord.isPresent()) return false;
        ProblemAcRecord newProblemACRecord = new ProblemAcRecord();
        newProblemACRecord.setProblemId(problem.getId());
        newProblemACRecord.setOjAccountId(ojAccount.getId());
        newProblemACRecord.setTime(new Timestamp(time));
        problemAcRecordRepository.save(newProblemACRecord);
        return true;
    }

    @Override
    public Problem findProblem(String ojName, String problemId) {
        return problemRepository.findByOjNameAndProblemId(ojName, problemId).get();
    }

    @Override
    public AcProblemInDay getProblemAcRecordInDay(List<Integer> ojAccounts, List<Integer> exOjAccounts, Date startTime, Date endTime) {
        AcProblemInDay acProblemInDay = new AcProblemInDay();
        acProblemInDay.setTime(new SimpleDateFormat("yyyy-MM-dd").format(startTime));
        log.info(acProblemInDay.getTime());
        acProblemInDay.setAcProblems(problemAcRecordRepository.findAcProblemByOjAccountsAndTimeBetweenAndExceptByOjAccounts(ojAccounts, startTime, endTime, exOjAccounts));
        return acProblemInDay;
    }

    @Override
    public List<AcProblemInDay> getProblemAcRecordSeveralDays(String studentId, Date time, int days, String exStudentId) {
        List<OjAccount> ojAccounts = ojAccountRepository.findAllByStudentId(studentId);
        List<Integer> ojAccountIds = new ArrayList<>();
        List<Integer> exOjAccountIds = new ArrayList<>();
        for(OjAccount ojAccount : ojAccounts) {
            ojAccountIds.add(ojAccount.getId());
        }
        List<OjAccount> exOjAccounts = new ArrayList<>();
        if (exStudentId != null) {
            exOjAccounts = ojAccountRepository.findAllByStudentId(exStudentId);
        }
        for(OjAccount ojAccount : exOjAccounts) {
            exOjAccountIds.add(ojAccount.getId());
        }
        List<AcProblemInDay> acProblemInDays = new ArrayList<>();
        if(ojAccountIds.size() == 0) return acProblemInDays;
        if(exOjAccountIds.size() == 0) {
            exOjAccountIds.add(0);
        }
        while (days > 0) {
            Date preTime;
            try {
                preTime = problemAcRecordRepository.findFirstByStudentIdAndTimeBeforeOrderByTimeDescAndStudentIdNotEquals(ojAccountIds, time, exOjAccountIds).get(0).getTime();
            } catch (Exception e) {
                return acProblemInDays;
            }
            log.info(String.valueOf(preTime));
            preTime.setHours(0);
            preTime.setMinutes(0);
            preTime.setSeconds(0);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(preTime);
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            time = calendar.getTime();
            AcProblemInDay acProblemInDay = getProblemAcRecordInDay(ojAccountIds, exOjAccountIds, preTime, time);
            if (acProblemInDay.getAcProblems().size() > 0) {
                days--;
                acProblemInDays.add(acProblemInDay);
            }
            time = preTime;
            if (preTime.compareTo(new Date(0)) <= 0) {
                break;
            }
        }
        return acProblemInDays;
    }

    @Override
    public List<PersonalProblemAcRank> getPersonalProblemAcRank(int grade, boolean includeRetired) {
        List<User> users;
        String status = "退役";
        if(includeRetired) {
            status = "";
        }
        if (grade == 0) {
            users = userRepository.findAllByStudentIdNotNullAndStatusNot(status);
        } else {
            users = userRepository.findAllByGradeAndStatusNot(grade, status);
        }
        List<PersonalProblemAcRank> personalProblemAcRanks = new ArrayList<>();
        for (User user : users) {
            PersonalProblemAcRank personalProblemAcRank = new PersonalProblemAcRank();
            personalProblemAcRank.setUser(user);
            personalProblemAcRank.setAcNum(problemAcRecordRepository.countAllByStudentId(user.getStudentId()));
            //todo getaward

            personalProblemAcRanks.add(personalProblemAcRank);
        }
        Collections.sort(personalProblemAcRanks);
        return personalProblemAcRanks;
    }


}
