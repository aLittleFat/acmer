package cn.edu.scau.acm.acmer.service;

import cn.edu.scau.acm.acmer.model.TeamWithUsers;

import java.util.List;

public interface TeamService {
    List<TeamWithUsers> getTeamBySeasonId(int seasonId);

    void addTeam(int seasonId, int rank, String vjAccount);

    void addStudent(int teamId, String studentId) throws Exception;

    void deleteStudent(int teamId, String studentId) throws Exception;

    void deleteTeam(Integer teamId) throws Exception;
}
