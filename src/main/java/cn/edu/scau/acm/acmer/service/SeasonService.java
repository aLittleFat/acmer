package cn.edu.scau.acm.acmer.service;

import cn.edu.scau.acm.acmer.entity.Season;
import cn.edu.scau.acm.acmer.entity.User;
import cn.edu.scau.acm.acmer.model.SeasonParticipant;
import com.alibaba.fastjson.JSONArray;

import java.util.List;

public interface SeasonService {
    List<Season> getAllSeason();

    void addSeason(int year, String name, String type, int cfPro) throws Exception;

    void deleteSeason(int seasonId) throws Exception;

    Season getSeasonById(int seasonId) throws Exception;

    List<User> getSeasonStudentBySeasonId(int seasonId);

    JSONArray getSeasonStudentChoiceBySeasonId(int seasonId);

    void addSeasonStudentBySeasonId(int seasonId, List<String> studentIds) throws Exception;

    void deleteSeasonStudentBySeasonIdAndStudentId(int seasonId, String studentId) throws Exception;

    JSONArray getTeamStudentChoiceBySeasonId(int seasonId);

    List<SeasonParticipant> getSeasonParticipantBySeasonId(Integer seasonId) throws Exception;
}
