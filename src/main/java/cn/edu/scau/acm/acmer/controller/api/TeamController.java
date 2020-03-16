package cn.edu.scau.acm.acmer.controller.api;

import cn.edu.scau.acm.acmer.entity.Team;
import cn.edu.scau.acm.acmer.model.MyResponseEntity;
import cn.edu.scau.acm.acmer.model.MyTeamMenu;
import cn.edu.scau.acm.acmer.model.TeamWithUsers;
import cn.edu.scau.acm.acmer.model.UserDto;
import cn.edu.scau.acm.acmer.repository.UserRepository;
import cn.edu.scau.acm.acmer.service.TeamService;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "api", produces = "application/json; charset=utf-8")
public class TeamController {
    @Autowired
    private TeamService teamService;

    @Autowired
    private UserRepository userRepository;

    @ApiOperation("给队伍添加队员")
    @RequiresRoles("admin")
    @PostMapping("team/{teamId}/student")
    MyResponseEntity<Void> addStudent(@PathVariable int teamId, String studentId) throws Exception {
        teamService.addStudent(teamId, studentId);
        return new MyResponseEntity<>();
    }

    @ApiOperation("给队伍删除队员")
    @RequiresRoles("admin")
    @DeleteMapping("team/{teamId}/student")
    MyResponseEntity<Void> deleteStudent(@PathVariable int teamId, String studentId) throws Exception {
        teamService.deleteStudent(teamId, studentId);
        return new MyResponseEntity<>();
    }

    @ApiOperation("删除队伍")
    @RequiresRoles("admin")
    @DeleteMapping("team")
    MyResponseEntity<Void> deleteTeam(Integer teamId) throws Exception {
        teamService.deleteTeam(teamId);
        return new MyResponseEntity<>();
    }

    @ApiOperation("获取我的队伍")
    @GetMapping("team")
    @RequiresRoles("student")
    MyResponseEntity<List<MyTeamMenu>> getTeam() {
        int id = ((UserDto) SecurityUtils.getSubject().getPrincipal()).getId();
        String studentId = userRepository.findById(id).get().getStudentId();
        return new MyResponseEntity<>(teamService.getTeamByStudentId(studentId));
    }

    @ApiOperation("获取队伍概览页面信息")
    @GetMapping("teamInfo/{teamId}")
    MyResponseEntity<JSONObject> getTeamInfoByTeamId(@PathVariable Integer teamId) throws Exception {
        String studentId = null;
        try {
            int id = ((UserDto) SecurityUtils.getSubject().getPrincipal()).getId();
            studentId = userRepository.findById(id).get().getStudentId();
        } catch (Exception ignored) { }
        return new MyResponseEntity(teamService.getTeamInfoByTeamIdAndStudentId(teamId, studentId));
    }

    @ApiOperation("修改队名")
    @PutMapping("team/{teamId}/teamName")
    @RequiresRoles("student")
    MyResponseEntity<Void> changeTeamName(@PathVariable Integer teamId, String nameCn, String nameEn) throws Exception {
        int id = ((UserDto) SecurityUtils.getSubject().getPrincipal()).getId();
        String studentId = userRepository.findById(id).get().getStudentId();
        teamService.changeTeamName(teamId, nameCn, nameEn, studentId);
        return new MyResponseEntity<>();
    }

}
