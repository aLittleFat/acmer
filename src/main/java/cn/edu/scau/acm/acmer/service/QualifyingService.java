package cn.edu.scau.acm.acmer.service;

import cn.edu.scau.acm.acmer.entity.Contest;
import cn.edu.scau.acm.acmer.entity.Qualifying;
import cn.edu.scau.acm.acmer.entity.ScoreRecordView;

import java.util.List;

public interface QualifyingService {
    List<Qualifying> getBySeasonId(Integer seasonId);

    void addQualifying(Integer seasonId, String title, String ojName, String cId, String password, Double proportion, Integer seasonAccountId) throws Exception;

    void deleteQualifying(Integer qualifyingId) throws Exception;

    List<ScoreRecordView> getQualifyingScoreByQualifyingId(Integer qualifyingId);

    Contest getContestByQualifyingId(Integer qualifyingId);
}
