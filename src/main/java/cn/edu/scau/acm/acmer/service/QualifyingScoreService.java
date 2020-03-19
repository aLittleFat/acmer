package cn.edu.scau.acm.acmer.service;

import cn.edu.scau.acm.acmer.model.Scores;

import java.util.List;

public interface QualifyingScoreService {
    void calcScore(Integer qualifyingId);

    List<Scores> getSumScore(Integer seasonId) throws Exception;
}
