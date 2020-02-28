package cn.edu.scau.acm.acmer.service.impl;

import cn.edu.scau.acm.acmer.entity.ProblemDifficult;
import cn.edu.scau.acm.acmer.repository.ProblemDifficultRepository;
import cn.edu.scau.acm.acmer.service.ProblemDifficultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class ProblemDifficultServiceImpl implements ProblemDifficultService {

    @Autowired
    private ProblemDifficultRepository problemDifficultRepository;

    @Override
    public void updateProblemDifficult(Integer problemId, String studentId, BigDecimal difficult) {
        Optional<ProblemDifficult> optionalProblemDifficult = problemDifficultRepository.findByProblemIdAndStudentId(problemId, studentId);
        ProblemDifficult problemDifficult;
        if(optionalProblemDifficult.isEmpty()) {
            problemDifficult = new ProblemDifficult();
            problemDifficult.setProblemId(problemId);
            problemDifficult.setStudentId(studentId);
        } else {
            problemDifficult = optionalProblemDifficult.get();
        }
        problemDifficult.setDifficult(difficult);
        problemDifficultRepository.save(problemDifficult);
    }
}
