package cn.edu.scau.acm.acmer.model;

import java.util.List;

public class Scores {
    private SeasonParticipant participant;
    private Double sumScore;
    private List<Double> scoreList;

    public SeasonParticipant getParticipant() {
        return participant;
    }

    public void setParticipant(SeasonParticipant participant) {
        this.participant = participant;
    }

    public Double getSumScore() {
        return sumScore;
    }

    public void setSumScore(Double sumScore) {
        this.sumScore = sumScore;
    }

    public List<Double> getScoreList() {
        return scoreList;
    }

    public void setScoreList(List<Double> scoreList) {
        this.scoreList = scoreList;
    }
}
