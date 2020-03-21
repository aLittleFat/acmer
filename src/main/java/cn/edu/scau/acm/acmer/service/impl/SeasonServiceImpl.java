package cn.edu.scau.acm.acmer.service.impl;

import cn.edu.scau.acm.acmer.entity.SeasonStudent;
import cn.edu.scau.acm.acmer.entity.Season;
import cn.edu.scau.acm.acmer.entity.User;
import cn.edu.scau.acm.acmer.model.SeasonParticipant;
import cn.edu.scau.acm.acmer.model.TeamContestRank;
import cn.edu.scau.acm.acmer.repository.*;
import cn.edu.scau.acm.acmer.service.SeasonService;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class SeasonServiceImpl implements SeasonService {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SeasonRepository seasonRepository;

    @Autowired
    private SeasonStudentRepository seasonStudentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ContestRecordViewRepository contestRecordViewRepository;

    @Autowired
    private AwardRepository awardRepository;

    @Override
    public List<Season> getAllSeason() {
        return seasonRepository.findAll();
    }

    @Override
    public void addSeason(int year, String name, String type, int cfPro) throws Exception {
        if(cfPro < 0 || cfPro > 100) {
            throw new Exception("cf比例越界");
        }
        Season season = new Season();
        season.setYear(year);
        season.setName(name);
        season.setType(type);
        season.setCfProportion(cfPro / 100.0);
        seasonRepository.save(season);
    }

    @Override
    public void deleteSeason(int seasonId) throws Exception {
        Optional<Season> season = seasonRepository.findById(seasonId);
        if(season.isEmpty()) {
            throw new Exception("不存在该赛季");
        }
        seasonRepository.delete(season.get());
    }

    @Override
    public Season getSeasonById(int seasonId) throws Exception {
        Optional<Season> season = seasonRepository.findById(seasonId);
        if (season.isEmpty()) {
            throw new Exception("赛季不存在");
        }
        return season.get();
    }

    @Override
    public List<User> getSeasonStudentBySeasonId(int seasonId) {
        return userRepository.findAllBySeasonId(seasonId);
    }

    @Override
    public JSONArray getSeasonStudentChoiceBySeasonId(int seasonId) {
        List<User> users = userRepository.findAllNotInSeasonBySeasonId(seasonId);
        return getChoiceArray(users);
    }

    @Override
    public JSONArray getTeamStudentChoiceBySeasonId(int seasonId) {
        List<User> users = userRepository.findAllNotInTeamBySeasonId(seasonId);
        return getChoiceArray(users);
    }

    @Override
    public List<SeasonParticipant> getSeasonParticipantBySeasonId(Integer seasonId) throws Exception {
        Optional<Season> optionalSeason = seasonRepository.findById(seasonId);
        if(optionalSeason.isEmpty()) {
            throw new Exception("不存在的赛季");
        }
        Season season = optionalSeason.get();
        List<SeasonParticipant> seasonParticipants;
        if(season.getType().equals("个人赛")) {
            seasonParticipants = seasonRepository.findAllSeasonStudentParticipantBySeasonId(seasonId);
        } else {
            seasonParticipants = seasonRepository.findAllSeasonTeamParticipantBySeasonId(seasonId);
        }
        return seasonParticipants;
    }

    @Override
    public List<Season> getAllTeamSeason() {
        return seasonRepository.findAllTeamSeason();
    }

    @Override
    public List<TeamContestRank> findAllTeamContestRankBySeasonId(Integer seasonId) {
        List<TeamContestRank> teamContestRanks = contestRecordViewRepository.findAllTeamContestRankBySeasonId(seasonId);
        for (TeamContestRank teamContestRank : teamContestRanks) {
            teamContestRank.setAwardList(awardRepository.findAllByTeamIdAndVerifiedOrderByTimeAsc(teamContestRank.getTeamId(), (byte)1));
        }
        return teamContestRanks;
    }

    private JSONArray getChoiceArray(List<User> users) {
        JSONArray studentChoicePerGrade = new JSONArray();
        int nowYear = 0;
        JSONObject studentChoice = null;
        JSONArray studentChoiceArray = null;
        for (User user : users) {
            if(user.getGrade() != nowYear) {
                if(studentChoiceArray != null) {
                    studentChoice.put("children", studentChoiceArray);
                    studentChoicePerGrade.add(studentChoice);
                }
                nowYear = user.getGrade();
                studentChoice = new JSONObject();
                studentChoiceArray = new JSONArray();
                studentChoice.put("title", String.valueOf(nowYear));
                studentChoice.put("expand", true);
            }
            JSONObject jsonStudent = new JSONObject();
            jsonStudent.put("title", user.getName());
            jsonStudent.put("id", user.getStudentId());
            studentChoiceArray.add(jsonStudent);
        }
        if(studentChoice != null) {
            studentChoicePerGrade.add(studentChoice);
            studentChoice.put("children", studentChoiceArray);
        }
        return studentChoicePerGrade;
    }

    @Override
    @Transactional
    public void addSeasonStudentBySeasonId(int seasonId, List<String> studentIds) throws Exception {
        Optional<Season> optionalSeason = seasonRepository.findById(seasonId);
        if(optionalSeason.isEmpty()) throw new Exception("不存在的赛季");
        Season season = optionalSeason.get();
        System.out.println(studentIds);
        if(!season.getType().equals("个人赛")) throw new Exception("非个人赛无法添加队员");
        for (String studentId : studentIds) {
            log.info(studentId);
            SeasonStudent seasonStudent = new SeasonStudent();
            seasonStudent.setStudentId(studentId);
            seasonStudent.setSeasonId(seasonId);
            seasonStudentRepository.save(seasonStudent);
        }
    }

    @Override
    public void deleteSeasonStudentBySeasonIdAndStudentId(int seasonId, String studentId) throws Exception {
        Optional<SeasonStudent> optionalSeasonStudent = seasonStudentRepository.findBySeasonIdAndStudentId(seasonId, studentId);
        if(optionalSeasonStudent.isEmpty()) throw new Exception("不存在改记录");
        seasonStudentRepository.delete(optionalSeasonStudent.get());
    }


}
