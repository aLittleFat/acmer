package cn.edu.scau.acm.acmer.controller.api;

import cn.edu.scau.acm.acmer.entity.Season;
import cn.edu.scau.acm.acmer.entity.SeasonAccount;
import cn.edu.scau.acm.acmer.entity.User;
import cn.edu.scau.acm.acmer.model.MyResponseEntity;
import cn.edu.scau.acm.acmer.model.SeasonParticipant;
import cn.edu.scau.acm.acmer.model.TeamWithUsers;
import cn.edu.scau.acm.acmer.repository.SeasonAccountRepository;
import cn.edu.scau.acm.acmer.service.SeasonAccountService;
import cn.edu.scau.acm.acmer.service.SeasonService;
import cn.edu.scau.acm.acmer.service.TeamService;
import com.alibaba.fastjson.JSONArray;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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

    @Autowired
    private SeasonAccountRepository seasonAccountRepository;

    @Autowired
    private SeasonAccountService seasonAccountService;

    @ApiOperation("获取赛季列表")
    @GetMapping("/season")
    MyResponseEntity<List<Season>> getSeason() {
        return new MyResponseEntity<>(seasonService.getAllSeason());
    }

    @ApiOperation("获取单个赛季")
    @GetMapping("/season/{seasonId}")
    MyResponseEntity<Season> getSeasonById(@PathVariable int seasonId) throws Exception {
        return new MyResponseEntity<>(seasonService.getSeasonById(seasonId));
    }

    @ApiOperation("添加赛季")
    @RequiresRoles({"admin"})
    @PostMapping("/season")
    MyResponseEntity<Void> addSeason(int year, String name, String type, int cfPro) throws Exception {
        seasonService.addSeason(year, name, type, cfPro);
        return new MyResponseEntity<>();
    }

    @ApiOperation("删除赛季")
    @RequiresRoles({"admin"})
    @DeleteMapping("/season")
    MyResponseEntity<Void> deleteSeason(int seasonId) throws Exception {
        seasonService.deleteSeason(seasonId);
        return new MyResponseEntity<>();
    }

    @ApiOperation("获取个人赛季的队员列表")
    @GetMapping("/season/{seasonId}/student")
    MyResponseEntity<List<User>> getSeasonStudentBySeasonId(@PathVariable int seasonId) {
        return new MyResponseEntity<>(seasonService.getSeasonStudentBySeasonId(seasonId));
    }

    @ApiOperation("添加个人赛季的参赛队员")
    @PostMapping("/season/{seasonId}/student")
    MyResponseEntity<Void> addSeasonStudentBySeasonId(@PathVariable int seasonId, @RequestParam List<String> studentIds) throws Exception {
        seasonService.addSeasonStudentBySeasonId(seasonId, studentIds);
        return new MyResponseEntity<>();
    }

    @ApiOperation("删除个人赛季的参赛队员")
    @DeleteMapping("/season/{seasonId}/student")
    MyResponseEntity<Void> deleteSeasonStudentBySeasonIdAndStudentId(@PathVariable int seasonId, String studentId) throws Exception {
        seasonService.deleteSeasonStudentBySeasonIdAndStudentId(seasonId, studentId);
        return new MyResponseEntity<>();
    }

    @ApiOperation("获取个人赛季可以添加的队员列表")
    @GetMapping("/season/{seasonId}/studentChoice")
    MyResponseEntity<JSONArray> getSeasonStudentChoiceBySeasonId(@PathVariable int seasonId) {
        return new MyResponseEntity<>(seasonService.getSeasonStudentChoiceBySeasonId(seasonId));
    }

    @ApiOperation("获取组队赛季可以组队的队员列表")
    @GetMapping("/season/{seasonId}/teamStudentChoice")
    MyResponseEntity<JSONArray> getTeamStudentChoiceBySeasonId(@PathVariable int seasonId) {
        return new MyResponseEntity<>(seasonService.getTeamStudentChoiceBySeasonId(seasonId));
    }

    @ApiOperation("获取赛季队伍列表")
    @GetMapping("/season/{seasonId}/team")
    MyResponseEntity<List<TeamWithUsers>> getTeamBySeasonId(@PathVariable int seasonId) {
        return new MyResponseEntity<>(teamService.getTeamBySeasonId(seasonId));
    }

    @ApiOperation("获取赛季队伍")
    @PostMapping("season/{seasonId}/team")
    MyResponseEntity<Void> addTeam(@PathVariable int seasonId, int rank, String vjAccount) {
        teamService.addTeam(seasonId, rank, vjAccount);
        return new MyResponseEntity<>();
    }

    @ApiOperation("获取赛季账号集")
    @GetMapping("season/{seasonId}/seasonAccount")
    MyResponseEntity<List<SeasonAccount>> getSeasonAccountBySeasonId(@PathVariable Integer seasonId) {
        return new MyResponseEntity<>(seasonAccountRepository.findAllBySeasonId(seasonId));
    }

    @ApiOperation("添加账号集")
    @PostMapping("season/{seasonId}/seasonAccount")
    MyResponseEntity<Void> addSeasonAccountBySeasonId(@PathVariable Integer seasonId, String title, @RequestParam List<Integer> seasonStudentIds, @RequestParam List<Integer> teamIds, @RequestParam List<String> handles, @RequestParam List<String> accounts, @RequestParam List<String> passwords) throws Exception {
        seasonAccountService.addSeasonAccount(seasonId, title, seasonStudentIds, teamIds, handles, accounts, passwords);
        return new MyResponseEntity<>();
    }

    @ApiOperation("删除账号集")
    @DeleteMapping("seasonAccount/{seasonAccountId}")
    MyResponseEntity<Void> deleteSeasonAccount(@PathVariable Integer seasonAccountId) throws Exception {
        seasonAccountService.deleteSeasonAccount(seasonAccountId);
        return new MyResponseEntity<>();
    }

    @ApiOperation("获取本赛季参赛者列表")
    @GetMapping("season/{seasonId}/participant")
    MyResponseEntity<List<SeasonParticipant>> getSeasonParticipantBySeasonId(@PathVariable Integer seasonId) throws Exception {
        return new MyResponseEntity<>(seasonService.getSeasonParticipantBySeasonId(seasonId));
    }

}
