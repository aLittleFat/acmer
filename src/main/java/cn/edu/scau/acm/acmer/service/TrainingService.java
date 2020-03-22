package cn.edu.scau.acm.acmer.service;

import com.alibaba.fastjson.JSONArray;

import java.util.List;

public interface TrainingService {
    void addTraining(String title, String ojName, String cId, String username, String password, String endTime) throws Exception;

    void deleteTraining(Integer trainingId) throws Exception;

    JSONArray getStudentChoiceByTrainingId(Integer trainingId);

    void addStudents(Integer trainingId, List<String> studentIds) throws Exception;

    void addTeams(Integer trainingId, List<Integer> teamIds) throws Exception;

    JSONArray getTeamChoiceByTrainingIdAndSeasonId(Integer trainingId, Integer seasonId);

    void deleteTrainingParticipant(Integer trainingParticipantId) throws Exception;
}
