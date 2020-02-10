package cn.edu.scau.acm.acmer.controller.api.student;

import cn.edu.scau.acm.acmer.model.MyResponseEntity;
import cn.edu.scau.acm.acmer.model.PersonalContestRank;
import cn.edu.scau.acm.acmer.model.UserDto;
import cn.edu.scau.acm.acmer.repository.StudentRepository;
import cn.edu.scau.acm.acmer.service.ContestService;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "api/student/contest", produces = "application/json; charset=utf-8")
public class S_ContestController {

    @Autowired
    private ContestService contestService;

    @Autowired
    private StudentRepository studentRepository;

    @GetMapping("getMyContest")
    MyResponseEntity<List<PersonalContestRank>> getMyContest(){
        try {
            int id = ((UserDto) SecurityUtils.getSubject().getPrincipal()).getId();
            String studentId = studentRepository.findByUserId(id).get().getId();
            return new MyResponseEntity<>(contestService.getPersonalContestRank(studentId));
        } catch (Exception e) {
            return new MyResponseEntity<>(e.getMessage());
        }
    }
}
