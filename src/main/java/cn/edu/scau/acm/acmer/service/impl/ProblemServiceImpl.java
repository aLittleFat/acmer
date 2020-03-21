package cn.edu.scau.acm.acmer.service.impl;

import cn.edu.scau.acm.acmer.entity.*;
import cn.edu.scau.acm.acmer.model.AcProblemInDay;
import cn.edu.scau.acm.acmer.model.PersonalProblemAcRank;
import cn.edu.scau.acm.acmer.repository.*;
import cn.edu.scau.acm.acmer.service.*;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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

    @Autowired
    private ProblemDifficultRepository problemDifficultRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private ProblemTagRepository problemTagRepository;

    @Autowired
    private AwardRepository awardRepository;

    @Autowired
    private ProblemViewRepository problemViewRepository;

    @Override
    public void addProblem(String ojName, String problemId, String title) {
        Optional<Problem> problem = problemRepository.findByOjNameAndProblemId(ojName, problemId);
        if(problem.isEmpty()) {
            ojService.addOj(ojName);
            Problem newProblem = new Problem();
            newProblem.setOjName(ojName);
            newProblem.setProblemId(problemId);
            newProblem.setTitle(title);
            problemRepository.save(newProblem);
        }
    }

    @Override
    public boolean addProblemAcRecord(Problem problem, OjAccount ojAccount, Long time) {
        Optional<ProblemAcRecord> optionalProblemAcRecord = problemAcRecordRepository.findProblemACRecordByOjAccountIdAndProblemId(ojAccount.getId(), problem.getId());
        if(optionalProblemAcRecord.isPresent()){
            ProblemAcRecord problemAcRecord = optionalProblemAcRecord.get();
            if (problemAcRecord.getTime().getTime() == time) {
                return true;
            } else {
                problemAcRecord.setTime(new Timestamp(time));
                problemAcRecordRepository.save(problemAcRecord);
                return false;
            }
        }
        ProblemAcRecord newProblemACRecord = new ProblemAcRecord();
        newProblemACRecord.setProblemId(problem.getId());
        newProblemACRecord.setOjAccountId(ojAccount.getId());
        newProblemACRecord.setTime(new Timestamp(time));
        problemAcRecordRepository.save(newProblemACRecord);
        return false;
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
    public List<PersonalProblemAcRank> getPersonalProblemAcRank(int grade) {
        List<User> users;
        if (grade == 0) {
            users = userRepository.findAllByStudentIdNotNull();
        } else {
            users = userRepository.findAllByGrade(grade);
        }
        List<PersonalProblemAcRank> personalProblemAcRanks = new ArrayList<>();
        for (User user : users) {
            PersonalProblemAcRank personalProblemAcRank = new PersonalProblemAcRank();
            personalProblemAcRank.setUser(user);
            personalProblemAcRank.setAcNum(problemAcRecordRepository.countAllByStudentId(user.getStudentId()));
            personalProblemAcRank.setAwards(awardRepository.findAllByStudentId(user.getStudentId()));
            personalProblemAcRanks.add(personalProblemAcRank);
        }
        Collections.sort(personalProblemAcRanks);
        return personalProblemAcRanks;
    }

    @Override
    public JSONObject getProblemInfo(int id, String studentId) {
        JSONObject res = new JSONObject();
        Problem problem = problemRepository.findById(id).get();
        JSONObject problemJson = new JSONObject();
        problemJson.put("id", problem.getOjName() + " " + problem.getProblemId());
        problemJson.put("title", problem.getTitle());
        problemJson.put("link", "https://vjudge.net/problem/" + problem.getOjName() + "-" + problem.getProblemId());
        res.put("problem", problemJson);
        BigDecimal difficult = problemDifficultRepository.avgByProblemId(id);
        if(difficult == null) difficult = BigDecimal.ZERO;
        res.put("difficult", difficult);
        res.put("tags", problemTagRepository.findAllByProblemId(id));

        boolean ac = checkIsAc(id, studentId);

        res.put("ac", ac);
        if(ac) {
            Optional<ProblemDifficult> problemDifficult = problemDifficultRepository.findByProblemIdAndStudentId(id, studentId);
            if(problemDifficult.isEmpty()) {
                difficult = BigDecimal.ZERO;
            } else {
                difficult = problemDifficult.get().getDifficult();
            }
            res.put("myDifficult", difficult);
            res.put("myTags", problemTagRepository.findAllByProblemIdAndStudentId(id, studentId));
        }
        return res;
    }



    @Override
    public boolean checkIsAc(int problemId, String studentId) {
        return problemAcRecordRepository.isAc(problemId, studentId) > 0;
    }

    @Override
    public Page<ProblemView> searchProblem(String key, BigDecimal minDifficult, BigDecimal maxDifficult, String tagName, Integer page, Integer size) {
        Pageable pr = PageRequest.of(page-1, size, Sort.Direction.ASC, "id");
        key = "%" + key + "%";
        tagName = "%" + tagName + "%";
        return problemViewRepository.findAllByOjNameLikeAndDifficultBetweenAndTagsLikeOrProblemIdLikeAndDifficultBetweenAndTagsLikeOrTitleLikeAndDifficultBetweenAndTagsLike(key, minDifficult, maxDifficult, tagName, key, minDifficult, maxDifficult, tagName, key, minDifficult, maxDifficult, tagName, pr);
    }


}
