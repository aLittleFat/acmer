package cn.edu.scau.acm.acmer.controller.api;

import cn.edu.scau.acm.acmer.entity.Contest;
import cn.edu.scau.acm.acmer.entity.Qualifying;
import cn.edu.scau.acm.acmer.entity.QualifyingScore;
import cn.edu.scau.acm.acmer.entity.ScoreRecordView;
import cn.edu.scau.acm.acmer.model.MyResponseEntity;
import cn.edu.scau.acm.acmer.model.Scores;
import cn.edu.scau.acm.acmer.service.QualifyingScoreService;
import cn.edu.scau.acm.acmer.service.QualifyingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "api", produces = "application/json; charset=utf-8")
public class QualifyingController {
    @Autowired
    private QualifyingService qualifyingService;

    @Autowired
    private QualifyingScoreService qualifyingScoreService;

    @GetMapping("season/{seasonId}/qualifying")
    MyResponseEntity<List<Qualifying>> getQualifyingBySeasonId(@PathVariable Integer seasonId) {
        return new MyResponseEntity<>(qualifyingService.getBySeasonId(seasonId));
    }

    @GetMapping("season/{seasonId}/qualifyingCalculated")
    MyResponseEntity<List<Qualifying>> getQualifyingBySeasonIdCalculated(@PathVariable Integer seasonId) {
        return new MyResponseEntity<>(qualifyingService.getBySeasonIdCalculated(seasonId));
    }

    @PostMapping("season/{seasonId}/baseQualifying")
    MyResponseEntity<Void> addBaseQualifying(@PathVariable Integer seasonId) throws Exception {
        qualifyingService.addBaseQualifying(seasonId);
        return new MyResponseEntity<>();
    }

    @PostMapping("season/{seasonId}/cfRatingQualifying")
    MyResponseEntity<Void> addCfQualifying(@PathVariable Integer seasonId) throws Exception {
        qualifyingService.addCfQualifying(seasonId);
        return new MyResponseEntity<>();
    }

    @PostMapping("season/{seasonId}/qualifying")
    MyResponseEntity<Void> addQualifyingBySeasonId(@PathVariable Integer seasonId, String title, String ojName, String cId, String password, Double proportion, Integer seasonAccountId) throws Exception {
        qualifyingService.addQualifying(seasonId, title, ojName, cId, password, proportion, seasonAccountId);
        return new MyResponseEntity<>();
    }

    @DeleteMapping("qualifying/{qualifyingId}")
    MyResponseEntity<Void> deleteQualifying(@PathVariable Integer qualifyingId) throws Exception {
        qualifyingService.deleteQualifying(qualifyingId);
        return new MyResponseEntity<>();
    }

    @GetMapping("qualifyingScore/{qualifyingId}")
    MyResponseEntity<List<ScoreRecordView>> getQualifyingScoreByQualifyingId(@PathVariable Integer qualifyingId) {
        return new MyResponseEntity<>(qualifyingService.getQualifyingScoreByQualifyingId(qualifyingId));
    }

    @GetMapping("qualifyingScore/{qualifyingId}/contest")
    MyResponseEntity<Contest> getContestByQualifyingId(@PathVariable Integer qualifyingId) {
        return new MyResponseEntity<>(qualifyingService.getContestByQualifyingId(qualifyingId));
    }

    @GetMapping("season/{seasonId}/sumScore")
    MyResponseEntity<List<Scores>> getSumScoreBySeasonId(@PathVariable Integer seasonId) throws Exception {
        return new MyResponseEntity<>(qualifyingScoreService.getSumScore(seasonId));
    }
}


