package cn.edu.scau.acm.acmer.model;

import cn.edu.scau.acm.acmer.entity.Contest;
import cn.edu.scau.acm.acmer.entity.ContestRecord;
import cn.edu.scau.acm.acmer.entity.ContestRecordView;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

public class MultiContestRecordLine {
    private Integer contestId;
    private String contestTitle;
    private Timestamp time;
    private Integer solvedNumber;
    private Integer penalty;
    private List<String> solved;
    private List<String> upSolved;
    private List<String> problemList;

    public MultiContestRecordLine(ContestRecordView contestRecordView) {
        this.contestId = contestRecordView.getContestId();
        this.contestTitle = contestRecordView.getTitle();
        this.time = contestRecordView.getTime();
        this.penalty = contestRecordView.getPenalty() / 60;
        this.solved = Arrays.asList(contestRecordView.getSolved().split(" "));
        this.upSolved = Arrays.asList(contestRecordView.getUpSolved().split(" "));
        this.problemList = Arrays.asList(contestRecordView.getProblemList().split(" "));
        this.solvedNumber = this.solved.size();
    }

    public Integer getContestId() {
        return contestId;
    }

    public void setContestId(Integer contestId) {
        this.contestId = contestId;
    }

    public String getContestTitle() {
        return contestTitle;
    }

    public void setContestTitle(String contestTitle) {
        this.contestTitle = contestTitle;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public Integer getSolvedNumber() {
        return solvedNumber;
    }

    public void setSolvedNumber(Integer solvedNumber) {
        this.solvedNumber = solvedNumber;
    }

    public List<String> getSolved() {
        return solved;
    }

    public void setSolved(List<String> solved) {
        this.solved = solved;
    }

    public List<String> getUpSolved() {
        return upSolved;
    }

    public void setUpSolved(List<String> upSolved) {
        this.upSolved = upSolved;
    }

    public List<String> getProblemList() {
        return problemList;
    }

    public void setProblemList(List<String> problemList) {
        this.problemList = problemList;
    }

    public Integer getPenalty() {
        return penalty;
    }

    public void setPenalty(Integer penalty) {
        this.penalty = penalty;
    }
}
