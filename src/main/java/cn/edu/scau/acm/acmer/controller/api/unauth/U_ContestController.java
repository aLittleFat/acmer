package cn.edu.scau.acm.acmer.controller.api.unauth;

import cn.edu.scau.acm.acmer.model.MyResponseEntity;
import cn.edu.scau.acm.acmer.model.PersonalContestLine;
import cn.edu.scau.acm.acmer.model.UserDto;
import cn.edu.scau.acm.acmer.service.ContestService;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "api/unauth/contest", produces = "application/json; charset=utf-8")
public class U_ContestController {

    @Autowired
    private ContestService contestService;

    @GetMapping("getPersonalContestByStudentId")
    MyResponseEntity<List<PersonalContestLine>> getPersonalContestByStudentId(String studentId){
        try {
            return new MyResponseEntity<>(contestService.getPersonalContestByStudentId(studentId));
        } catch (Exception e) {
            return new MyResponseEntity<>(e.getMessage());
        }
    }
}
