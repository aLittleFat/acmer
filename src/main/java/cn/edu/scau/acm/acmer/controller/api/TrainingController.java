package cn.edu.scau.acm.acmer.controller.api;

import cn.edu.scau.acm.acmer.entity.Training;
import cn.edu.scau.acm.acmer.entity.TrainingParticipant;
import cn.edu.scau.acm.acmer.entity.TrainingParticipantView;
import cn.edu.scau.acm.acmer.entity.TrainingRecordView;
import cn.edu.scau.acm.acmer.model.MyResponseEntity;
import cn.edu.scau.acm.acmer.repository.TrainingParticipantRepository;
import cn.edu.scau.acm.acmer.repository.TrainingParticipantViewRepository;
import cn.edu.scau.acm.acmer.repository.TrainingRepository;
import cn.edu.scau.acm.acmer.service.TrainingService;
import com.alibaba.fastjson.JSONArray;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api", produces = "application/json; charset=utf-8")
public class TrainingController {

    @Autowired
    private TrainingService trainingService;

    @Autowired
    private TrainingRepository trainingRepository;

    @Autowired
    private TrainingParticipantRepository trainingParticipantRepository;

    @Autowired
    private TrainingParticipantViewRepository trainingParticipantViewRepository;

    @GetMapping("training")
    MyResponseEntity<List<Training>> getAllTraining() {
        return new MyResponseEntity<>(trainingRepository.findAll());
    }

    @GetMapping("training/{trainingId}")
    MyResponseEntity<Training> getTrainingById(@PathVariable Integer trainingId) {
        return new MyResponseEntity<>(trainingRepository.findById(trainingId).get());
    }

    @PostMapping("training")
    @RequiresRoles("admin")
    MyResponseEntity<Void> addTraining(String title, String ojName, String cId, String username, String password, String endTime) throws Exception {
        trainingService.addTraining(title, ojName, cId, username, password, endTime);
        return new MyResponseEntity<>();
    }

    @DeleteMapping("training")
    MyResponseEntity<Void> deleteTraining(Integer trainingId) throws Exception {
        trainingService.deleteTraining(trainingId);
        return new MyResponseEntity<>();
    }

    @GetMapping("trainingParticipant")
    MyResponseEntity<List<TrainingParticipantView>> getAllTrainingParticipant(Integer trainingId) {
        return new MyResponseEntity<>(trainingParticipantViewRepository.findAllByTrainingId(trainingId));
    }

    @GetMapping("trainingParticipant/unFinished")
    MyResponseEntity<List<TrainingParticipantView>> getAllTrainingParticipantUnFinished(Integer trainingId) {
        return new MyResponseEntity<>(trainingService.findAllUnFinishedByTrainingId(trainingId));
    }

    @DeleteMapping("trainingParticipant")
    MyResponseEntity<Void> deleteTrainingParticipant(Integer trainingParticipantId) throws Exception {
        trainingService.deleteTrainingParticipant(trainingParticipantId);
        return new MyResponseEntity<>();
    }

    @GetMapping("/training/{trainingId}/studentChoice")
    MyResponseEntity<JSONArray> getStudentChoiceByTrainingId(@PathVariable Integer trainingId) {
        return new MyResponseEntity<>(trainingService.getStudentChoiceByTrainingId(trainingId));
    }

    @GetMapping("/training/{trainingId}/teamChoice")
    MyResponseEntity<JSONArray> getTeamChoiceByTrainingIdAndSeasonId(@PathVariable Integer trainingId, Integer seasonId) {
        return new MyResponseEntity<>(trainingService.getTeamChoiceByTrainingIdAndSeasonId(trainingId, seasonId));
    }

    @PutMapping("/training/{trainingId}/student")
    MyResponseEntity<Void> addStudents(@PathVariable Integer trainingId, @RequestParam List<String> studentIds) throws Exception {
        trainingService.addStudents(trainingId, studentIds);
        return new MyResponseEntity<>();
    }

    @PutMapping("/training/{trainingId}/team")
    MyResponseEntity<Void> addTeams(@PathVariable Integer trainingId, @RequestParam List<Integer> teamIds) throws Exception {
        trainingService.addTeams(trainingId, teamIds);
        return new MyResponseEntity<>();
    }

    @GetMapping("/training/{trainingId}/record")
    MyResponseEntity<List<TrainingRecordView>> getTrainingRecord(@PathVariable Integer trainingId) {
        return new MyResponseEntity<>(trainingService.getTrainingRecord(trainingId));
    }
}
