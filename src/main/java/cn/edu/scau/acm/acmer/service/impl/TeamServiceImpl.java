package cn.edu.scau.acm.acmer.service.impl;

import cn.edu.scau.acm.acmer.entity.*;
import cn.edu.scau.acm.acmer.model.MyTeamMenu;
import cn.edu.scau.acm.acmer.model.TeamWithUsers;
import cn.edu.scau.acm.acmer.repository.AwardRepository;
import cn.edu.scau.acm.acmer.repository.TeamRepository;
import cn.edu.scau.acm.acmer.repository.TeamStudentRepository;
import cn.edu.scau.acm.acmer.repository.UserRepository;
import cn.edu.scau.acm.acmer.service.MailService;
import cn.edu.scau.acm.acmer.service.TeamService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TeamServiceImpl implements TeamService {

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TeamStudentRepository teamStudentRepository;

    @Autowired
    private AwardRepository awardRepository;

    @Autowired
    private MailService mailService;

    @Override
    public List<TeamWithUsers> getTeamBySeasonId(int seasonId) {
        List<Team> teams = teamRepository.findAllBySeasonIdOrderByRank(seasonId);
        List<TeamWithUsers> teamWithUsersList = new ArrayList<>();
        for (Team team : teams) {
            List<User> users = userRepository.findAllByTeamId(team.getId());
            TeamWithUsers teamWithUsers = new TeamWithUsers(team, users);
            teamWithUsersList.add(teamWithUsers);
        }
        return teamWithUsersList;
    }

    @Override
    public void addTeam(int seasonId, int rank, String vjAccount) {
        Team team = new Team();
        team.setSeasonId(seasonId);
        team.setRank(rank);
        team.setVjAccount(vjAccount);
        teamRepository.save(team);
    }

    @Override
    public void addStudent(int teamId, String studentId) throws Exception {
        Optional<Team> team = teamRepository.findById(teamId);
        if(team.isEmpty()) {
            throw new Exception("不存在的队伍");
        }
        Optional<User> user = userRepository.findByStudentId(studentId);
        if(user.isEmpty()) {
            throw new Exception("不存在的队员");
        }
        TeamStudentPK teamStudentPK = new TeamStudentPK();
        teamStudentPK.setStudentId(studentId);
        teamStudentPK.setTeamId(teamId);
        if(teamStudentRepository.findById(teamStudentPK).isPresent()) {
            throw new Exception("该队员已加入该队伍");
        }
        if(teamStudentRepository.countByStudentIdAndSeasonId(studentId, team.get().getSeasonId()) > 0) {
            throw new Exception("该队员已加入本赛季其他队伍");
        }
        if(teamStudentRepository.countAllByTeamId(teamId) >= 3) {
            throw new Exception("队伍已满人");
        }
        TeamStudent teamStudent = new TeamStudent();
        teamStudent.setTeamId(teamId);
        teamStudent.setStudentId(studentId);
        teamStudentRepository.save(teamStudent);
    }

    @Override
    public void deleteStudent(int teamId, String studentId) throws Exception {
        TeamStudentPK teamStudentPK = new TeamStudentPK();
        teamStudentPK.setTeamId(teamId);
        teamStudentPK.setStudentId(studentId);
        Optional<TeamStudent> teamStudent = teamStudentRepository.findById(teamStudentPK);
        if(teamStudent.isEmpty()) {
            throw new Exception("不存在该记录");
        }
        teamStudentRepository.delete(teamStudent.get());
    }

    @Override
    public void deleteTeam(Integer teamId) throws Exception {
        Optional<Team> team = teamRepository.findById(teamId);
        if(team.isEmpty()) {
            throw new Exception("不存在该队伍");
        }
        teamRepository.delete(team.get());
    }

    @Override
    public List<MyTeamMenu> getTeamByStudentId(String studentId) {
        return teamRepository.findAllTeamMenuByStudentId(studentId);
    }

    @Override
    public JSONObject getTeamInfoByTeamIdAndStudentId(Integer teamId, String studentId) throws Exception {
        Optional<Team> optionalTeam = teamRepository.findById(teamId);
        if(optionalTeam.isEmpty()) {
            throw new Exception("不存在的队伍");
        }
        JSONObject res = new JSONObject();
        boolean isMyTeam = checkInTeam(teamId, studentId);
        res.put("isMyTeam", isMyTeam);

        Team team = optionalTeam.get();
        List<User> students = userRepository.findAllByTeamId(teamId);
        List<Award> awards;
        if(isMyTeam) {
            awards = awardRepository.findAllByTeamIdOrderByTimeAsc(teamId);
        } else {
            awards = awardRepository.findAllByTeamIdAndVerifiedOrderByTimeAsc(teamId, (byte)1);
        }
        res.put("team", team);
        res.put("students", students);
        res.put("awards", awards);
        return res;
    }

    @Override
    public void changeTeamName(Integer teamId, String nameCn, String nameEn, String studentId) throws Exception {
        Optional<Team> optionalTeam = teamRepository.findById(teamId);
        if(optionalTeam.isEmpty()) {
            throw new Exception("不存在的队伍");
        }
        Team team = optionalTeam.get();
        if(!checkInTeam(teamId, studentId)) {
            throw new Exception("不是你的队伍");
        }
        team.setNameCn(nameCn);
        team.setNameEn(nameEn);
        teamRepository.save(team);
    }

    @Override
    public boolean checkInTeam(Integer teamId, String studentId) {
        TeamStudentPK teamStudentPK = new TeamStudentPK();
        teamStudentPK.setTeamId(teamId);
        teamStudentPK.setStudentId(studentId);
        return teamStudentRepository.findById(teamStudentPK).isPresent();
    }

    @Override
    public void sendMail(Integer teamId, String title, String msg) {
        List<User> users = userRepository.findAllByTeamId(teamId);
        for (User user : users) {
            mailService.sendTextMail(user.getEmail(), title, msg);
        }
    }
}
