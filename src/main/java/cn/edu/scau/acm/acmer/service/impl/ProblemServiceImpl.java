package cn.edu.scau.acm.acmer.service.impl;

import cn.edu.scau.acm.acmer.entity.OJAccount;
import cn.edu.scau.acm.acmer.entity.Problem;
import cn.edu.scau.acm.acmer.entity.ProblemACRecord;
import cn.edu.scau.acm.acmer.repository.ProblemACRecordRepository;
import cn.edu.scau.acm.acmer.repository.ProblemRepository;
import cn.edu.scau.acm.acmer.service.ProblemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class ProblemServiceImpl implements ProblemService {

    @Autowired
    ProblemRepository problemRepository;

    @Autowired
    ProblemACRecordRepository problemACRecordRepository;

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
        ProblemACRecord problemACRecord = problemACRecordRepository.findProblemACRecordByOjAccountIdAndAndProblemId(ojAccount.getId(), problem.getId());
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
    


}
