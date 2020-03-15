package cn.edu.scau.acm.acmer.model;

import cn.edu.scau.acm.acmer.entity.Contest;
import cn.edu.scau.acm.acmer.entity.ContestRecord;
import cn.edu.scau.acm.acmer.entity.ContestRecordView;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

public class ContestRecordLine implements Comparable<ContestRecordLine> {
    private Timestamp time;
    private Integer solvedNumber;
    private Integer penalty;
    private List<String> solved;
    private List<String> upSolved;
    private String participants;
    private Integer teamId;
    private String studentId;

    public ContestRecordLine(ContestRecordView contestRecordView) {
        this.time = contestRecordView.getTime();
        this.penalty = contestRecordView.getPenalty() / 60;
        this.solved = Arrays.asList(contestRecordView.getSolved().split(" "));
        this.upSolved = Arrays.asList(contestRecordView.getUpSolved().split(" "));
        this.solvedNumber = this.solved.size();
        this.participants = contestRecordView.getStudents();
        this.teamId = contestRecordView.getTeamId();
        this.studentId = contestRecordView.getStudentId();
    }



    public List<String> getUpSolved() {
        return upSolved;
    }

    public void setUpSolved(List<String> upSolved) {
        this.upSolved = upSolved;
    }

    public List<String> getSolved() {
        return solved;
    }

    public void setSolved(List<String> solved) {
        this.solved = solved;
    }

    public Integer getPenalty() {
        return penalty;
    }

    public void setPenalty(Integer penalty) {
        this.penalty = penalty;
    }

    public Integer getSolvedNumber() {
        return solvedNumber;
    }

    public void setSolvedNumber(Integer solvedNumber) {
        this.solvedNumber = solvedNumber;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public String getParticipants() {
        return participants;
    }

    public void setParticipants(String participants) {
        this.participants = participants;
    }

    @Override
    public int compareTo(ContestRecordLine contestRecordLine) {
        if(this.solvedNumber.equals(contestRecordLine.getSolvedNumber())) {
            if(this.penalty.equals(contestRecordLine.getPenalty())) {
                return this.participants.compareTo(contestRecordLine.participants);
            }
            return this.penalty.compareTo(contestRecordLine.getPenalty());
        }
        return -this.solvedNumber.compareTo(contestRecordLine.getSolvedNumber());
    }

    public Integer getTeamId() {
        return teamId;
    }

    public void setTeamId(Integer teamId) {
        this.teamId = teamId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }
}
