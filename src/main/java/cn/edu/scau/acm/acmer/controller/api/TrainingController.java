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
import io.swagger.annotations.ApiOperation;
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

    @ApiOperation("获取所有训练的列表")
    @GetMapping("training")
    MyResponseEntity<List<Training>> getAllTraining() {
        return new MyResponseEntity<>(trainingRepository.findAll());
    }

    @ApiOperation("获取单个训练的信息")
    @GetMapping("training/{trainingId}")
    MyResponseEntity<Training> getTrainingById(@PathVariable Integer trainingId) {
        return new MyResponseEntity<>(trainingRepository.findById(trainingId).get());
    }

    @ApiOperation("添加训练")
    @PostMapping("training")
    @RequiresRoles("admin")
    MyResponseEntity<Void> addTraining(String title, String ojName, String cId, String username, String password, String endTime) throws Exception {
        trainingService.addTraining(title, ojName, cId, username, password, endTime);
        return new MyResponseEntity<>();
    }

    @ApiOperation("删除训练")
    @DeleteMapping("training")
    MyResponseEntity<Void> deleteTraining(Integer trainingId) throws Exception {
        trainingService.deleteTraining(trainingId);
        return new MyResponseEntity<>();
    }

    @ApiOperation("获取训练参与者")
    @GetMapping("trainingParticipant")
    MyResponseEntity<List<TrainingParticipantView>> getAllTrainingParticipant(Integer trainingId) {
        return new MyResponseEntity<>(trainingParticipantViewRepository.findAllByTrainingId(trainingId));
    }

    @ApiOperation("获取还未完成训练的队员/队伍列表")
    @GetMapping("trainingParticipant/unFinished")
    MyResponseEntity<List<TrainingParticipantView>> getAllTrainingParticipantUnFinished(Integer trainingId) {
        return new MyResponseEntity<>(trainingService.findAllUnFinishedByTrainingId(trainingId));
    }

    @ApiOperation("删除训练参与者")
    @DeleteMapping("trainingParticipant")
    MyResponseEntity<Void> deleteTrainingParticipant(Integer trainingParticipantId) throws Exception {
        trainingService.deleteTrainingParticipant(trainingParticipantId);
        return new MyResponseEntity<>();
    }

    @ApiOperation("获取可以加入训练的队员")
    @GetMapping("/training/{trainingId}/studentChoice")
    MyResponseEntity<JSONArray> getStudentChoiceByTrainingId(@PathVariable Integer trainingId) {
        return new MyResponseEntity<>(trainingService.getStudentChoiceByTrainingId(trainingId));
    }

    @ApiOperation("获取可以加入训练的队员队伍")
    @GetMapping("/training/{trainingId}/teamChoice")
    MyResponseEntity<JSONArray> getTeamChoiceByTrainingIdAndSeasonId(@PathVariable Integer trainingId, Integer seasonId) {
        return new MyResponseEntity<>(trainingService.getTeamChoiceByTrainingIdAndSeasonId(trainingId, seasonId));
    }

    @ApiOperation("训练添加队员")
    @RequiresRoles("admin")
    @PutMapping("/training/{trainingId}/student")
    MyResponseEntity<Void> addStudents(@PathVariable Integer trainingId, @RequestParam List<String> studentIds) throws Exception {
        trainingService.addStudents(trainingId, studentIds);
        return new MyResponseEntity<>();
    }

    @ApiOperation("训练添加队伍")
    @RequiresRoles("admin")
    @PutMapping("/training/{trainingId}/team")
    MyResponseEntity<Void> addTeams(@PathVariable Integer trainingId, @RequestParam List<Integer> teamIds) throws Exception {
        trainingService.addTeams(trainingId, teamIds);
        return new MyResponseEntity<>();
    }

    @ApiOperation("获取训练中已完成的队员/队伍的记录")
    @GetMapping("/training/{trainingId}/record")
    MyResponseEntity<List<TrainingRecordView>> getTrainingRecord(@PathVariable Integer trainingId) {
        return new MyResponseEntity<>(trainingService.getTrainingRecord(trainingId));
    }
}
