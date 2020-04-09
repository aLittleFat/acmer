package cn.edu.scau.acm.acmer.controller.api;

import cn.edu.scau.acm.acmer.entity.Contest;
import cn.edu.scau.acm.acmer.entity.Qualifying;
import cn.edu.scau.acm.acmer.entity.QualifyingScore;
import cn.edu.scau.acm.acmer.entity.ScoreRecordView;
import cn.edu.scau.acm.acmer.model.MyResponseEntity;
import cn.edu.scau.acm.acmer.model.QualifyingAcChart;
import cn.edu.scau.acm.acmer.model.Scores;
import cn.edu.scau.acm.acmer.service.QualifyingScoreService;
import cn.edu.scau.acm.acmer.service.QualifyingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresRoles;
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

    @ApiOperation("获取一个赛季的排位赛")
    @GetMapping("season/{seasonId}/qualifying")
    MyResponseEntity<List<Qualifying>> getQualifyingBySeasonId(@PathVariable Integer seasonId) {
        return new MyResponseEntity<>(qualifyingService.getBySeasonId(seasonId));
    }

    @ApiOperation("获取一个赛季的已计算积分的排位赛")
    @GetMapping("season/{seasonId}/qualifyingCalculated")
    MyResponseEntity<List<Qualifying>> getQualifyingBySeasonIdCalculated(@PathVariable Integer seasonId) {
        return new MyResponseEntity<>(qualifyingService.getBySeasonIdCalculated(seasonId));
    }

    @ApiOperation("添加基础分")
    @RequiresRoles("admin")
    @PostMapping("season/{seasonId}/baseQualifying")
    MyResponseEntity<Void> addBaseQualifying(@PathVariable Integer seasonId) throws Exception {
        qualifyingService.addBaseQualifying(seasonId);
        return new MyResponseEntity<>();
    }

    @ApiOperation("添加cf积分")
    @RequiresRoles("admin")
    @PostMapping("season/{seasonId}/cfRatingQualifying")
    MyResponseEntity<Void> addCfQualifying(@PathVariable Integer seasonId) throws Exception {
        qualifyingService.addCfQualifying(seasonId);
        return new MyResponseEntity<>();
    }

    @ApiOperation("从OJ添加排位赛")
    @RequiresRoles("admin")
    @PostMapping("season/{seasonId}/qualifying")
    MyResponseEntity<Void> addQualifyingBySeasonId(@PathVariable Integer seasonId, String title, String ojName, String cId, String password, Double proportion, Integer seasonAccountId) throws Exception {
        qualifyingService.addQualifying(seasonId, title, ojName, cId, password, proportion, seasonAccountId);
        return new MyResponseEntity<>();
    }

    @ApiOperation("删除排位赛")
    @RequiresRoles("admin")
    @DeleteMapping("qualifying/{qualifyingId}")
    MyResponseEntity<Void> deleteQualifying(@PathVariable Integer qualifyingId) throws Exception {
        qualifyingService.deleteQualifying(qualifyingId);
        return new MyResponseEntity<>();
    }

    @ApiOperation("获取一场排位赛的积分")
    @GetMapping("qualifyingScore/{qualifyingId}")
    MyResponseEntity<List<ScoreRecordView>> getQualifyingScoreByQualifyingId(@PathVariable Integer qualifyingId) {
        return new MyResponseEntity<>(qualifyingService.getQualifyingScoreByQualifyingId(qualifyingId));
    }

    @ApiOperation("获取一场排位赛对应的竞赛信息")
    @GetMapping("qualifyingScore/{qualifyingId}/contest")
    MyResponseEntity<Contest> getContestByQualifyingId(@PathVariable Integer qualifyingId) {
        return new MyResponseEntity<>(qualifyingService.getContestByQualifyingId(qualifyingId));
    }

    @ApiOperation("获取一个赛季的总积分")
    @GetMapping("season/{seasonId}/sumScore")
    MyResponseEntity<List<Scores>> getSumScoreBySeasonId(@PathVariable Integer seasonId) throws Exception {
        return new MyResponseEntity<>(qualifyingScoreService.getSumScore(seasonId));
    }

    @ApiOperation("获取AC榜的图表数据")
    @GetMapping("season/{seasonId}/acChart")
    MyResponseEntity<List<QualifyingAcChart>> getQualifyingAcChartBySeasonId(@PathVariable Integer seasonId) throws Exception {
        return new MyResponseEntity<>(qualifyingService.getQualifyingAcChartBySeasonId(seasonId));
    }

    @ApiOperation("更新赛季的积分")
    @RequiresRoles("admin")
    @PutMapping("season/{seasonId}/qualifyingContestRecord")
    MyResponseEntity<Void> updateSeasonQualifying(@PathVariable Integer seasonId) {
        qualifyingService.updateSeasonQualifying(seasonId);
        return new MyResponseEntity<>();
    }
}


