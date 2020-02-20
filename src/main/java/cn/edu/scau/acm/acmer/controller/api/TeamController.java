package cn.edu.scau.acm.acmer.controller.api;

import cn.edu.scau.acm.acmer.model.MyResponseEntity;
import cn.edu.scau.acm.acmer.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api", produces = "application/json; charset=utf-8")
public class TeamController {
    @Autowired
    private TeamService teamService;

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

}
