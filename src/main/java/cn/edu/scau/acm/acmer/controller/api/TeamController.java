package cn.edu.scau.acm.acmer.controller.api;

import cn.edu.scau.acm.acmer.entity.Team;
import cn.edu.scau.acm.acmer.model.MyResponseEntity;
import cn.edu.scau.acm.acmer.model.MyTeamMenu;
import cn.edu.scau.acm.acmer.model.UserDto;
import cn.edu.scau.acm.acmer.repository.UserRepository;
import cn.edu.scau.acm.acmer.service.TeamService;
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

    @PostMapping("team/{teamId}/student")
    MyResponseEntity<Void> addStudent(@PathVariable int teamId, String studentId) throws Exception {
        teamService.addStudent(teamId, studentId);
        return new MyResponseEntity<>();
    }

    @DeleteMapping("team/{teamId}/student")
    MyResponseEntity<Void> deleteStudent(@PathVariable int teamId, String studentId) throws Exception {
        teamService.deleteStudent(teamId, studentId);
        return new MyResponseEntity<>();
    }

    @DeleteMapping("team")
    MyResponseEntity<Void> deleteTeam(Integer teamId) throws Exception {
        teamService.deleteTeam(teamId);
        return new MyResponseEntity<>();
    }

    @GetMapping("team")
    @RequiresRoles("student")
    MyResponseEntity<List<MyTeamMenu>> getTeam() {
        int id = ((UserDto) SecurityUtils.getSubject().getPrincipal()).getId();
        String studentId = userRepository.findById(id).get().getStudentId();
        return new MyResponseEntity<>(teamService.getTeamByStudentId(studentId));
    }

}
