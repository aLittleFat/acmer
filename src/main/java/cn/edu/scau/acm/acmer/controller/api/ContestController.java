package cn.edu.scau.acm.acmer.controller.api;

import cn.edu.scau.acm.acmer.entity.TeamStudentPK;
import cn.edu.scau.acm.acmer.model.MyResponseEntity;
import cn.edu.scau.acm.acmer.model.UserDto;
import cn.edu.scau.acm.acmer.repository.TeamStudentRepository;
import cn.edu.scau.acm.acmer.repository.UserRepository;
import cn.edu.scau.acm.acmer.service.ContestService;
import com.alibaba.fastjson.JSONObject;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "api", produces = "application/json; charset=utf-8")
public class ContestController {

    Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ContestService contestService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TeamStudentRepository teamStudentRepository;

    @GetMapping("personalContest")
    @RequiresRoles({"student"})
    MyResponseEntity<JSONObject> getMyContest(){
        int id = ((UserDto) SecurityUtils.getSubject().getPrincipal()).getId();
        String studentId = userRepository.findById(id).get().getStudentId();
        JSONObject res = contestService.getContestByStudentId(studentId);
        return new MyResponseEntity<>(res);
    }

    @GetMapping("personalContest/{studentId}")
    MyResponseEntity<JSONObject> getPersonalContestByStudentId(@PathVariable String studentId){

        long a = System.currentTimeMillis();
        JSONObject res = contestService.getContestByStudentId(studentId);
        log.info(System.currentTimeMillis()-a+"ms");

        return new MyResponseEntity<>(res);
    }

    @GetMapping("teamContest/{teamId}")
    MyResponseEntity<JSONObject> getTeamContestByStudentId(@PathVariable Integer teamId){
        return new MyResponseEntity<>(contestService.getContestByTeamId(teamId));
    }

    @PutMapping("personalContestRecord")
    @RequiresRoles("student")
    MyResponseEntity<Void> addPersonalContestRecord(String ojName, String cId, String account, String password) throws Exception {
        int id = ((UserDto) SecurityUtils.getSubject().getPrincipal()).getId();
        String studentId = userRepository.findById(id).get().getStudentId();
        contestService.addContestRecord(ojName, cId, studentId, null, account, password);
        return new MyResponseEntity<>();
    }

    @PutMapping("teamContestRecord")
    @RequiresRoles("student")
    MyResponseEntity<Void> addTeamContestRecord(String ojName, String cId, Integer teamId, String account, String password) throws Exception {
        int id = ((UserDto) SecurityUtils.getSubject().getPrincipal()).getId();
        String studentId = userRepository.findById(id).get().getStudentId();
        TeamStudentPK teamStudentPK = new TeamStudentPK();
        teamStudentPK.setStudentId(studentId);
        teamStudentPK.setTeamId(teamId);
        if(teamStudentRepository.findById(teamStudentPK).isEmpty()) {
            throw new Exception("不是自己的队伍");
        }
        contestService.addContestRecord(ojName, cId, null, teamId, account, password);
        return new MyResponseEntity<>();
    }
}
