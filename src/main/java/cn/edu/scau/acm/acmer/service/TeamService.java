package cn.edu.scau.acm.acmer.service;

import cn.edu.scau.acm.acmer.entity.Team;
import cn.edu.scau.acm.acmer.model.MyTeamMenu;
import cn.edu.scau.acm.acmer.model.TeamAccount;
import cn.edu.scau.acm.acmer.model.TeamWithUsers;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

public interface TeamService {
    List<TeamWithUsers> getTeamBySeasonId(int seasonId);

    void addTeam(int seasonId, int rank, String vjAccount);

    void addStudent(int teamId, String studentId) throws Exception;

    void deleteStudent(int teamId, String studentId) throws Exception;

    void deleteTeam(Integer teamId) throws Exception;

    List<MyTeamMenu> getTeamByStudentId(String studentId);

    JSONObject getTeamInfoByTeamIdAndStudentId(Integer teamId, String studentId) throws Exception;

    void changeTeamName(Integer teamId, String nameCn, String nameEn, String studentId) throws Exception;

    boolean checkInTeam(Integer teamId, String studentId);

    void sendMail(Integer teamId, String title, String msg);

    List<TeamAccount> getTeamAccountByTeamId(Integer teamId, String studentId) throws Exception;
}
