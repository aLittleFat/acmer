package cn.edu.scau.acm.acmer.service.impl;

import cn.edu.scau.acm.acmer.entity.*;
import cn.edu.scau.acm.acmer.repository.*;
import cn.edu.scau.acm.acmer.service.ContestService;
import cn.edu.scau.acm.acmer.service.TrainingService;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TrainingServiceImpl implements TrainingService {

    @Autowired
    private ContestService contestService;

    @Autowired
    private ContestRepository contestRepository;

    @Autowired
    private TrainingRepository trainingRepository;

    @Autowired
    private TrainingParticipantRepository trainingParticipantRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private TeamStudentViewRepository teamStudentViewRepository;

    @Autowired
    private TrainingRecordViewRepository trainingRecordViewRepository;

    @Autowired
    private TrainingParticipantViewRepository trainingParticipantViewRepository;

    @Override
    public void addTraining(String title, String ojName, String cId, String username, String password, String endTime) throws Exception {
        contestService.addContest(ojName, cId, username, password);

        Contest contest = contestRepository.findByOjNameAndCid(ojName, cId).get();

        Training training = new Training();
        training.setContestId(contest.getId());
        training.setTitle(title);
        endTime = endTime.substring(0, endTime.indexOf('.')).replace('T', ' ');
        Timestamp timestamp = new Timestamp(Timestamp.valueOf(endTime).getTime() + 40*60*60*1000);
        if (timestamp.getTime() < contest.getEndTime().getTime()) {
            throw new Exception("截止时间不要早于比赛结束时间");
        }
        training.setEndTime(timestamp);
        trainingRepository.save(training);

    }

    @Override
    public void deleteTraining(Integer trainingId) throws Exception {
        Optional<Training> training = trainingRepository.findById(trainingId);
        if(training.isEmpty()) {
            throw new Exception("不存在的训练计划");
        }
        trainingRepository.delete(training.get());
    }

    @Override
    public JSONArray getStudentChoiceByTrainingId(Integer trainingId) {
        List<User> users = userRepository.findAllNotInTrainingByTrainingId(trainingId);
        return getChoiceArray(users);
    }

    @Override
    public void addStudents(Integer trainingId, List<String> studentIds) throws Exception {
        Optional<Training> training = trainingRepository.findById(trainingId);
        if(training.isEmpty()) {
            throw new Exception("不存在的训练计划");
        }
        for (String studentId : studentIds) {
            if (trainingParticipantRepository.findByTrainingIdAndStudentIdAndTeamId(trainingId, studentId, null).isPresent()) continue;
            TrainingParticipant trainingParticipant = new TrainingParticipant();
            trainingParticipant.setStudentId(studentId);
            trainingParticipant.setTrainingId(trainingId);
            trainingParticipantRepository.save(trainingParticipant);
        }
    }

    @Override
    public void addTeams(Integer trainingId, List<Integer> teamIds) throws Exception {
        Optional<Training> training = trainingRepository.findById(trainingId);
        if(training.isEmpty()) {
            throw new Exception("不存在的训练计划");
        }
        for (Integer teamId : teamIds) {
            if (trainingParticipantRepository.findByTrainingIdAndStudentIdAndTeamId(trainingId, null, teamId).isPresent()) continue;
            TrainingParticipant trainingParticipant = new TrainingParticipant();
            trainingParticipant.setTeamId(teamId);
            trainingParticipant.setTrainingId(trainingId);
            trainingParticipantRepository.save(trainingParticipant);
        }
    }

    @Override
    public JSONArray getTeamChoiceByTrainingIdAndSeasonId(Integer trainingId, Integer seasonId) {
        List<Team> teams = teamRepository.findAllBySeasonIdOrderByRankNumAsc(seasonId);
        JSONArray jsonArray = new JSONArray();
        for (Team team : teams) {
            if (trainingParticipantRepository.findByTrainingIdAndStudentIdAndTeamId(trainingId, null, team.getId()).isPresent()) continue;
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("title", teamStudentViewRepository.findById(team.getId()).get().getStudents());
            jsonObject.put("isTeam", true);
            jsonObject.put("id", team.getId());
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

    @Override
    public void deleteTrainingParticipant(Integer trainingParticipantId) throws Exception {
        Optional<TrainingParticipant> trainingParticipant = trainingParticipantRepository.findById(trainingParticipantId);
        if(trainingParticipant.isEmpty()) {
            throw new Exception("不存在的参赛者");
        }
        trainingParticipantRepository.delete(trainingParticipant.get());
    }

    @Override
    public List<TrainingRecordView> getTrainingRecord(Integer trainingId) {
        List<TrainingRecordView> trainingRecordViews = trainingRecordViewRepository.findAllByTrainingId(trainingId);
        trainingRecordViews.sort((o1,o2) -> {
            if(o1.getSolvedNumber().equals(o2.getSolvedNumber())) {
                return o1.getPenalty().compareTo(o2.getPenalty());
            } else {
                return o2.getSolvedNumber().compareTo(o1.getSolvedNumber());
            }
        });
        return trainingRecordViews;
    }

    @Override
    public List<TrainingParticipantView> findAllUnFinishedByTrainingId(Integer trainingId) {
        List<TrainingParticipantView> trainingParticipantViews = trainingParticipantViewRepository.findAllByTrainingId(trainingId);
        List<TrainingParticipantView> res = new ArrayList<>();
        for (TrainingParticipantView trainingParticipantView : trainingParticipantViews) {
            if (trainingRecordViewRepository.findByTrainingIdAndTrainingParticipantId(trainingId, trainingParticipantView.getId()).isEmpty()) {
                res.add(trainingParticipantView);
            }
        }
        return res;
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
}
