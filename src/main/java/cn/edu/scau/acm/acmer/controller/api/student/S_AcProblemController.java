package cn.edu.scau.acm.acmer.controller.api.student;

import cn.edu.scau.acm.acmer.model.AcProblemInDay;
import cn.edu.scau.acm.acmer.model.UserDto;
import cn.edu.scau.acm.acmer.repository.StudentRepository;
import cn.edu.scau.acm.acmer.service.AccountService;
import cn.edu.scau.acm.acmer.service.ProblemService;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "api/student/acProblem", produces = "application/json; charset=utf-8")
public class S_AcProblemController {
    @Autowired
    ProblemService problemService;

    @Autowired
    AccountService accountService;

    @Autowired
    StudentRepository studentRepository;

    @GetMapping("getMyAcProblems")
    List<AcProblemInDay> getMyAcProblems(long time, int days){
        int id = ((UserDto) SecurityUtils.getSubject().getPrincipal()).getId();
        String studentId = studentRepository.findByUserId(id).get().getId();
        return problemService.getProblemAcRecordSeveralDays(studentId,new Date(time), days,null);
    }
}
