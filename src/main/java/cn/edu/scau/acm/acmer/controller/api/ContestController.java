package cn.edu.scau.acm.acmer.controller.api;

import cn.edu.scau.acm.acmer.model.MyResponseEntity;
import cn.edu.scau.acm.acmer.model.UserDto;
import cn.edu.scau.acm.acmer.repository.UserRepository;
import cn.edu.scau.acm.acmer.service.ContestService;
import com.alibaba.fastjson.JSONObject;
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
    private UserRepository userRepository;

    @GetMapping("personalContest")
    @RequiresRoles({"student"})
    MyResponseEntity<JSONObject> getMyContest(){
        int id = ((UserDto) SecurityUtils.getSubject().getPrincipal()).getId();
        String studentId = userRepository.findById(id).get().getStudentId();
        return new MyResponseEntity<>(contestService.getContestByStudentId(studentId));
    }

    @GetMapping("personalContest/{studentId}")
    MyResponseEntity<JSONObject> getPersonalContestByStudentId(@PathVariable String studentId){
        return new MyResponseEntity<>(contestService.getContestByStudentId(studentId));
    }
}
