package cn.edu.scau.acm.acmer.controller.api;

import cn.edu.scau.acm.acmer.model.MyResponseEntity;
import cn.edu.scau.acm.acmer.model.PersonalContestLine;
import cn.edu.scau.acm.acmer.model.UserDto;
import cn.edu.scau.acm.acmer.repository.StudentRepository;
import cn.edu.scau.acm.acmer.service.ContestService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "api", produces = "application/json; charset=utf-8")
public class ContestController {
    @Autowired
    private ContestService contestService;

    @Autowired
    private StudentRepository studentRepository;

    @GetMapping("personalContest")
    @RequiresRoles({"student"})
    MyResponseEntity<List<PersonalContestLine>> getMyContest(){
        int id = ((UserDto) SecurityUtils.getSubject().getPrincipal()).getId();
        String studentId = studentRepository.findByUserId(id).get().getId();
        return new MyResponseEntity<>(contestService.getPersonalContestByStudentId(studentId));
    }

    @GetMapping("personalContest/{studentId}")
    MyResponseEntity<List<PersonalContestLine>> getPersonalContestByStudentId(@PathVariable String studentId){
        return new MyResponseEntity<>(contestService.getPersonalContestByStudentId(studentId));
    }
}
