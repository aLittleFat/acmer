package cn.edu.scau.acm.acmer.service;

import cn.edu.scau.acm.acmer.entity.Contest;
import cn.edu.scau.acm.acmer.entity.Qualifying;
import cn.edu.scau.acm.acmer.entity.ScoreRecordView;
import cn.edu.scau.acm.acmer.model.QualifyingAcChart;

import java.util.List;

public interface QualifyingService {
    List<Qualifying> getBySeasonId(Integer seasonId);

    List<Qualifying> getBySeasonIdCalculated(Integer seasonId);

    void addQualifying(Integer seasonId, String title, String ojName, String cId, String password, Double proportion, Integer seasonAccountId) throws Exception;

    void addBaseQualifying(Integer seasonId) throws Exception;

    void addCfQualifying(Integer seasonId) throws Exception;

    void deleteQualifying(Integer qualifyingId) throws Exception;

    List<ScoreRecordView> getQualifyingScoreByQualifyingId(Integer qualifyingId);

    Contest getContestByQualifyingId(Integer qualifyingId);

    List<QualifyingAcChart> getQualifyingAcChartBySeasonId(Integer seasonId);

    void updateSeasonQualifying(Integer seasonId);
}
