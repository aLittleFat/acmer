package cn.edu.scau.acm.acmer.service.impl;

import cn.edu.scau.acm.acmer.entity.OjAccount;
import cn.edu.scau.acm.acmer.entity.Problem;
import cn.edu.scau.acm.acmer.entity.ProblemAcRecord;
import cn.edu.scau.acm.acmer.entity.Student;
import cn.edu.scau.acm.acmer.model.AcProblem;
import cn.edu.scau.acm.acmer.model.AcProblemInDay;
import cn.edu.scau.acm.acmer.model.PersonalProblemAcRank;
import cn.edu.scau.acm.acmer.repository.*;
import cn.edu.scau.acm.acmer.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
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
    private BzojService bzojService;

    @Autowired
    private CfService cfService;

    @Autowired
    private HduService hduService;

    @Autowired
    private VjService vjService;

    @Autowired
    private OjAccountRepository ojAccountRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void addProblem(String ojName, String problemId) {
        Optional<Problem> problem = problemRepository.findByOjNameAndProblemId(ojName, problemId);
        if(!problem.isPresent()) {
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
    public AcProblemInDay getProblemAcRecordInDay(List<OjAccount> ojAccounts, List<OjAccount> exOjAccounts, Date startTime, Date endTime) {
        AcProblemInDay acProblemInDay = new AcProblemInDay();
        acProblemInDay.setTime(new SimpleDateFormat("yyyy-MM-dd").format(startTime));
        log.info(acProblemInDay.getTime());

        for(OjAccount ojAccount : ojAccounts) {
            List<AcProblem> acProblems = problemAcRecordRepository.findAcProblemByOjAccountIdAndTimeBetween(ojAccount.getId(), startTime, endTime);
            for(AcProblem acProblem: acProblems) {
                boolean add = true;

                for(OjAccount exOjAccount : exOjAccounts) {
                    if(problemAcRecordRepository.findProblemACRecordByOjAccountIdAndProblemId(exOjAccount.getId(), acProblem.getProblem().getId()).isPresent()) {
                        add = false;
                        break;
                    }
                }

                if(add) {
                    acProblemInDay.getAcProblems().add(acProblem);
                }
            }
        }

        return acProblemInDay;
    }

    @Override
    public List<AcProblemInDay> getProblemAcRecordSeveralDays(String studentId, Date time, int days, String exStudentId) {
        log.info(time.toString());
        List<OjAccount> ojAccounts = ojAccountRepository.findAllByStudentId(studentId);
        List<OjAccount> exOjAccounts = new ArrayList<>();
        if(exStudentId != null) {
            exOjAccounts = ojAccountRepository.findAllByStudentId(exStudentId);
        }
        List<AcProblemInDay> acProblemInDays = new ArrayList<>();
        while(days > 0) {
            Date preTime = new Date(0);
            for(OjAccount ojAccount : ojAccounts) {
                Optional<ProblemAcRecord> problemACRecord = problemAcRecordRepository.findFirstByOjAccountIdAndTimeBeforeOrderByTimeDesc(ojAccount.getId(), time);
                if(problemACRecord.isPresent() && problemACRecord.get().getTime().after(preTime)) {
                    preTime = problemACRecord.get().getTime();
                }
            }
            preTime.setHours(0);
            preTime.setMinutes(0);
            preTime.setSeconds(0);
            Calendar calendar=Calendar.getInstance();
            calendar.setTime(preTime);
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            time = calendar.getTime();
            AcProblemInDay acProblemInDay = getProblemAcRecordInDay(ojAccounts, exOjAccounts, preTime, time);
            if(acProblemInDay.getAcProblems().size() > 0) {
                days--;
                acProblemInDays.add(acProblemInDay);
            }
            time = preTime;
            if(preTime.compareTo(new Date(0)) <= 0) {
                break;
            }
        }
        return acProblemInDays;
    }

    @Override
    @Scheduled(cron = "0 49 21 * * ?")
    public void getAllAcProblemsFromOj() {
        bzojService.getAllAcProblems();
        cfService.getAllAcProblems();
        hduService.getAllAcProblems();
        vjService.getAllAcProblems();
    }

    @Override
    public List<PersonalProblemAcRank> getPersonalProblemAcRank(int grade, boolean includeRetired) {
        List<Student> students;
        if(grade == 0) {
            students = studentRepository.findAll();
        } else {
            students = studentRepository.findAllByGrade(grade);
        }
        List<PersonalProblemAcRank> personalProblemAcRanks = new ArrayList<>();
        for (Student student : students) {
            PersonalProblemAcRank personalProblemAcRank = new PersonalProblemAcRank();
            personalProblemAcRank.setUser(userRepository.findById((int)student.getUserId()).get());
            personalProblemAcRank.setStudent(student);
            personalProblemAcRank.setAcNum(problemAcRecordRepository.countAllByStudentId(student.getId()));
            //todo getaward

            personalProblemAcRanks.add(personalProblemAcRank);
        }
        Collections.sort(personalProblemAcRanks);
        return personalProblemAcRanks;
    }


}
