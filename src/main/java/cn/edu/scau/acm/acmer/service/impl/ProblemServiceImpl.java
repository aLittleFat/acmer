package cn.edu.scau.acm.acmer.service.impl;

import cn.edu.scau.acm.acmer.entity.OJAccount;
import cn.edu.scau.acm.acmer.entity.Problem;
import cn.edu.scau.acm.acmer.entity.ProblemACRecord;
import cn.edu.scau.acm.acmer.entity.Student;
import cn.edu.scau.acm.acmer.model.AcProblem;
import cn.edu.scau.acm.acmer.model.AcProblemInDay;
import cn.edu.scau.acm.acmer.model.PersonalProblemAcRank;
import cn.edu.scau.acm.acmer.repository.*;
import cn.edu.scau.acm.acmer.service.*;
import net.bytebuddy.asm.Advice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor;
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
    private ProblemACRecordRepository problemACRecordRepository;

    @Autowired
    private BzojService bzojService;

    @Autowired
    private CfService cfService;

    @Autowired
    private HduService hduService;

    @Autowired
    private VjService vjService;

    @Autowired
    private OJAccountRepository ojAccountRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void addProblem(String ojName, String problemId) {
        Problem problem = problemRepository.findByOjNameAndProblemId(ojName, problemId);
        if(problem == null) {
            problem = new Problem();
            problem.setOjName(ojName);
            problem.setProblemId(problemId);
            problemRepository.save(problem);
        }
    }

    @Override
    public boolean addProblemAcRecord(Problem problem, OJAccount ojAccount, Long time) {
        ProblemACRecord problemACRecord = problemACRecordRepository.findProblemACRecordByOjAccountIdAndProblemId(ojAccount.getId(), problem.getId());
        if(problemACRecord != null) return false;
        problemACRecord = new ProblemACRecord();
        problemACRecord.setProblemId(problem.getId());
        problemACRecord.setOjAccountId(ojAccount.getId());
        problemACRecord.setTime(new Timestamp(time));
        problemACRecordRepository.save(problemACRecord);
        return true;
    }

    @Override
    public Problem findProblem(String ojName, String problemId) {
        return problemRepository.findByOjNameAndProblemId(ojName, problemId);
    }

    @Override
    public AcProblemInDay getProblemAcRecordInDay(List<OJAccount> ojAccounts, List<OJAccount> exOjAccounts, Date startTime, Date endTime) {
        log.info(startTime.toString());
        log.info(endTime.toString());
        AcProblemInDay acProblemInDay = new AcProblemInDay();
        acProblemInDay.setTime(new SimpleDateFormat("yyyy-MM-dd").format(startTime));
        log.info(acProblemInDay.getTime());

        for(OJAccount ojAccount : ojAccounts) {
            List<AcProblem> acProblems = problemACRecordRepository.findAcProblemByOjAccountIdAndTimeBetween(ojAccount.getId(), startTime, endTime);
            for(AcProblem acProblem: acProblems) {
                boolean add = true;

                for(OJAccount exOjAccount : exOjAccounts) {
                    if(problemACRecordRepository.findProblemACRecordByOjAccountIdAndProblemId(exOjAccount.getId(), acProblem.getProblem().getId()) != null) {
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
        List<OJAccount> ojAccounts = ojAccountRepository.findAllByStudentId(studentId);
        List<OJAccount> exOjAccounts = new ArrayList<>();
        if(exStudentId != null) {
            exOjAccounts = ojAccountRepository.findAllByStudentId(exStudentId);
        }
        List<AcProblemInDay> acProblemInDays = new ArrayList<>();
        while(days > 0) {
            Date preTime = new Date(0);
            for(OJAccount ojAccount : ojAccounts) {
                ProblemACRecord problemACRecord = problemACRecordRepository.findFirstByOjAccountIdAndTimeBeforeOrderByTimeDesc(ojAccount.getId(), time);
                if(problemACRecord != null && problemACRecord.getTime().after(preTime)) {
                    preTime = problemACRecord.getTime();
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
    public int getAcNumByStudentId(String id) {

        return 0;
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
            personalProblemAcRank.setUser(userRepository.findById((int)student.getUserId()));
            personalProblemAcRank.setStudent(student);
            personalProblemAcRank.setAcNum(problemACRecordRepository.countAllByStudnetId(student.getId()));
            //todo getaward

            personalProblemAcRanks.add(personalProblemAcRank);
        }
        Collections.sort(personalProblemAcRanks);
        return personalProblemAcRanks;
    }


}
