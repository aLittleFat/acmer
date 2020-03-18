package cn.edu.scau.acm.acmer.service.impl;

import cn.edu.scau.acm.acmer.entity.Season;
import cn.edu.scau.acm.acmer.entity.Team;
import cn.edu.scau.acm.acmer.entity.User;
import cn.edu.scau.acm.acmer.model.StudentInfo;
import cn.edu.scau.acm.acmer.model.TagAcChart;
import cn.edu.scau.acm.acmer.repository.*;
import cn.edu.scau.acm.acmer.service.StudentService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProblemAcRecordRepository problemAcRecordRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private SeasonRepository seasonRepository;

    @Autowired
    private AwardRepository awardRepository;

    @Override
    public StudentInfo getStudentInfo(String studentId) {
        StudentInfo studentInfo = new StudentInfo();
        User user = userRepository.findByStudentId(studentId).get();
        studentInfo.setName(user.getName());
        studentInfo.setGrade(user.getGrade());
        studentInfo.setAwardList(awardRepository.findAllByStudentId(studentId));
        //todo studentInfo.setCfRating();
        List<Team> teams = teamRepository.findAllByStudentId(studentId);
        List<JSONObject> jsonTeams = new ArrayList<>();
        for (Team team : teams) {
            JSONObject jsonTeam = new JSONObject();
            Season season = seasonRepository.findById(team.getSeasonId()).get();
            jsonTeam.put("seasonName", season.getName());
            jsonTeam.put("teamName", team.getNameCn());
            jsonTeam.put("id", team.getId());
            jsonTeams.add(jsonTeam);
        }

        studentInfo.setTeams(jsonTeams);
        studentInfo.setAcNumber(problemAcRecordRepository.countAllByStudentId(studentId));
        studentInfo.setOjAcCharts(problemAcRecordRepository.countAllByStudentIdGroupByOJ(studentId));
        List<TagAcChart> tagAcCharts = problemAcRecordRepository.countAllByStudentIdGroupByTag(studentId);
        tagAcCharts.removeIf(tagAcChart -> tagAcChart.getTagName() == null);
        studentInfo.setTagAcCharts(tagAcCharts);
        return studentInfo;
    }
}
