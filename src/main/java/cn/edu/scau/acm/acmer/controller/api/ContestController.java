package cn.edu.scau.acm.acmer.controller.api;

import cn.edu.scau.acm.acmer.entity.TeamStudentPK;
import cn.edu.scau.acm.acmer.model.ContestTable;
import cn.edu.scau.acm.acmer.model.MyResponseEntity;
import cn.edu.scau.acm.acmer.model.UserDto;
import cn.edu.scau.acm.acmer.repository.TeamStudentRepository;
import cn.edu.scau.acm.acmer.repository.UserRepository;
import cn.edu.scau.acm.acmer.service.ContestService;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @ApiOperation("获取我的竞赛记录表格")
    @GetMapping("personalContest")
    @RequiresRoles({"student"})
    MyResponseEntity<JSONObject> getMyContest(){
        int id = ((UserDto) SecurityUtils.getSubject().getPrincipal()).getId();
        String studentId = userRepository.findById(id).get().getStudentId();
        JSONObject res = contestService.getContestTableByStudentId(studentId);
        return new MyResponseEntity<>(res);
    }

    @ApiOperation("获取指定队员的竞赛记录表格")
    @GetMapping("personalContest/{studentId}")
    MyResponseEntity<JSONObject> getPersonalContestByStudentId(@PathVariable String studentId){
        JSONObject res = contestService.getContestTableByStudentId(studentId);
        return new MyResponseEntity<>(res);
    }

    @ApiOperation("获取指定队伍的竞赛记录表格")
    @GetMapping("teamContest/{teamId}")
    MyResponseEntity<JSONObject> getTeamContestByStudentId(@PathVariable Integer teamId){
        String studentId = null;
        try {
            int id = ((UserDto) SecurityUtils.getSubject().getPrincipal()).getId();
            studentId = userRepository.findById(id).get().getStudentId();
        } catch (Exception ignore) { }
        return new MyResponseEntity<>(contestService.getContestTableByTeamId(teamId, studentId));
    }

    @ApiOperation("申报个人竞赛")
    @PutMapping("personalContestRecord")
    @RequiresRoles("student")
    MyResponseEntity<Void> addPersonalContestRecord(String ojName, String cId, String account, String password) throws Exception {
        int id = ((UserDto) SecurityUtils.getSubject().getPrincipal()).getId();
        String studentId = userRepository.findById(id).get().getStudentId();
        contestService.addContestRecord(ojName, cId, studentId, null, account, password);
        return new MyResponseEntity<>();
    }

    @ApiOperation("申报队伍竞赛")
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

    @ApiOperation("获取竞赛表格")
    @GetMapping("contestTable")
    MyResponseEntity<ContestTable> getContestTableByContestId(Integer contestId) throws Exception {
        return new MyResponseEntity<>(contestService.getContestTableByContestId(contestId));
    }

    @ApiOperation("修改题解")
    @PutMapping("solution")
    @RequiresRoles("student")
    MyResponseEntity<Void> changeSolution(Integer contestRecordId, String solution) throws Exception {
        contestService.changeSolution(contestRecordId, solution);
        return new MyResponseEntity<>();
    }

    @ApiOperation("获取比赛名称和链接")
    @GetMapping("contestInfo")
    MyResponseEntity<JSONObject> getContestInfo(Integer contestId) {
        return new MyResponseEntity<>(contestService.getContestInfo(contestId));
    }
}
