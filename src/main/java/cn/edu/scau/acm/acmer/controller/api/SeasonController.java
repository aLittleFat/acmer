package cn.edu.scau.acm.acmer.controller.api;

import cn.edu.scau.acm.acmer.entity.Season;
import cn.edu.scau.acm.acmer.entity.User;
import cn.edu.scau.acm.acmer.model.MyResponseEntity;
import cn.edu.scau.acm.acmer.model.TeamWithUsers;
import cn.edu.scau.acm.acmer.service.SeasonService;
import cn.edu.scau.acm.acmer.service.TeamService;
import com.alibaba.fastjson.JSONArray;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "api", produces = "application/json; charset=utf-8")
public class SeasonController {

    @Autowired
    private SeasonService seasonService;

    @Autowired
    private TeamService teamService;

    @GetMapping("/season")
    MyResponseEntity<List<Season>> getSeason() {
        return new MyResponseEntity<>(seasonService.getAllSeason());
    }

    @GetMapping("/season/{seasonId}")
    MyResponseEntity<Season> getSeasonById(@PathVariable int seasonId) throws Exception {
        return new MyResponseEntity<>(seasonService.getSeasonById(seasonId));
    }

    @RequiresRoles({"admin"})
    @PostMapping("/season")
    MyResponseEntity<Void> addSeason(int year, String name, String type, int cfPro) throws Exception {
        seasonService.addSeason(year, name, type, cfPro);
        return new MyResponseEntity<>();
    }

    @RequiresRoles({"admin"})
    @DeleteMapping("/season")
    MyResponseEntity<Void> deleteSeason(int seasonId) throws Exception {
        seasonService.deleteSeason(seasonId);
        return new MyResponseEntity<>();
    }

    @GetMapping("/season/{seasonId}/student")
    MyResponseEntity<List<User>> getSeasonStudentBySeasonId(@PathVariable int seasonId) {
        return new MyResponseEntity<>(seasonService.getSeasonStudentBySeasonId(seasonId));
    }

    @PostMapping("/season/{seasonId}/student")
    MyResponseEntity<Void> addSeasonStudentBySeasonId(@PathVariable int seasonId, @RequestParam List<String> studentIds) throws Exception {
        seasonService.addSeasonStudentBySeasonId(seasonId, studentIds);
        return new MyResponseEntity<>();
    }

    @DeleteMapping("/season/{seasonId}/student")
    MyResponseEntity<Void> deleteSeasonStudentBySeasonIdAndStudentId(@PathVariable int seasonId, String studentId) throws Exception {
        seasonService.deleteSeasonStudentBySeasonIdAndStudentId(seasonId, studentId);
        return new MyResponseEntity<>();
    }

    @GetMapping("/season/{seasonId}/studentChoice")
    MyResponseEntity<JSONArray> getSeasonStudentChoiceBySeasonId(@PathVariable int seasonId) {
        return new MyResponseEntity<>(seasonService.getSeasonStudentChoiceBySeasonId(seasonId));
    }

    @GetMapping("/season/{seasonId}/teamStudentChoice")
    MyResponseEntity<JSONArray> getTeamStudentChoiceBySeasonId(@PathVariable int seasonId) {
        return new MyResponseEntity<>(seasonService.getTeamStudentChoiceBySeasonId(seasonId));
    }

    @GetMapping("/season/{seasonId}/team")
    MyResponseEntity<List<TeamWithUsers>> getTeamBySeasonId(@PathVariable int seasonId) {
        return new MyResponseEntity<>(teamService.getTeamBySeasonId(seasonId));
    }

    @PostMapping("season/{seasonId}/team")
    MyResponseEntity<Void> addTeam(@PathVariable int seasonId, int rank, String vjAccount) {
        teamService.addTeam(seasonId, rank, vjAccount);
        return new MyResponseEntity<>();
    }

}
