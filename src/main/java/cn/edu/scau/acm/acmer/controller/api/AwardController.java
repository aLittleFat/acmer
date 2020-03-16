package cn.edu.scau.acm.acmer.controller.api;

import cn.edu.scau.acm.acmer.entity.AwardView;
import cn.edu.scau.acm.acmer.model.MyResponseEntity;
import cn.edu.scau.acm.acmer.model.UserDto;
import cn.edu.scau.acm.acmer.repository.AwardRepository;
import cn.edu.scau.acm.acmer.repository.UserRepository;
import cn.edu.scau.acm.acmer.service.AwardService;
import cn.edu.scau.acm.acmer.service.TeamService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/api"  ,produces = "application/json; charset=utf-8")
public class AwardController {

    Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AwardService awardService;

    @Autowired
    private TeamService teamService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("award")
    @RequiresRoles("student")
    MyResponseEntity<Void> addAward(String level, String regional, String time, String contestType, Integer teamId) throws Exception {
        int id = ((UserDto) SecurityUtils.getSubject().getPrincipal()).getId();
        String studentId = userRepository.findById(id).get().getStudentId();
        if(!teamService.checkInTeam(teamId, studentId)) {
            throw new Exception("不是你的队伍");
        }
        awardService.addAward(level, regional, time, contestType, teamId);
        return new MyResponseEntity<>();
    }

    @DeleteMapping("award/{awardId}")
    @RequiresRoles("student")
    MyResponseEntity<Void> deleteAward(@PathVariable Integer awardId) throws Exception {
        int id = ((UserDto) SecurityUtils.getSubject().getPrincipal()).getId();
        String studentId = userRepository.findById(id).get().getStudentId();
        awardService.deleteAward(awardId, studentId);
        return new MyResponseEntity<>();
    }
}
