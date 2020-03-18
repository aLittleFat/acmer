package cn.edu.scau.acm.acmer.controller.api;

import cn.edu.scau.acm.acmer.model.AcProblemInDay;
import cn.edu.scau.acm.acmer.model.MyResponseEntity;
import cn.edu.scau.acm.acmer.model.PersonalProblemAcRank;
import cn.edu.scau.acm.acmer.model.UserDto;
import cn.edu.scau.acm.acmer.repository.UserRepository;
import cn.edu.scau.acm.acmer.service.ProblemService;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "api", produces = "application/json; charset=utf-8")
public class AcProblemController {

    @Autowired
    private ProblemService problemService;

    @Autowired
    private UserRepository userRepository;

    @ApiOperation("获取刷题排行榜")
    @GetMapping("personalProblemAcRank")
    MyResponseEntity<List<PersonalProblemAcRank>> getPersonalProblemAcRank(int grade) {
        return new MyResponseEntity<>(problemService.getPersonalProblemAcRank(grade));
    }

    @ApiOperation("根据学号获取刷题记录")
    @GetMapping("acProblems/{studentId}")
    MyResponseEntity<List<AcProblemInDay>> getAcProblems(@PathVariable String studentId, boolean except, long time, int days){
        String mystudentId = null;
        if(except) {
            try {
                int id = ((UserDto) SecurityUtils.getSubject().getPrincipal()).getId();
                mystudentId = userRepository.findById(id).get().getStudentId();
            } catch (Exception ignored) { }
        }
        return new MyResponseEntity<>(problemService.getProblemAcRecordSeveralDays(studentId,new Date(time), days, mystudentId));
    }

    @ApiOperation("获取我的刷题记录")
    @RequiresRoles({"student"})
    @GetMapping("acProblems")
    MyResponseEntity<List<AcProblemInDay>> getMyAcProblems(long time, int days){
        int id = ((UserDto) SecurityUtils.getSubject().getPrincipal()).getId();
        String studentId = userRepository.findById(id).get().getStudentId();
        return new MyResponseEntity<>(problemService.getProblemAcRecordSeveralDays(studentId,new Date(time), days,null));
    }
}
