package cn.edu.scau.acm.acmer.model;

import cn.edu.scau.acm.acmer.entity.ContestProblemRecord;

import java.util.Date;
import java.util.List;

public class PersonalContestLine implements Comparable<PersonalContestLine> {
    private int contestId;
    private String title;
    private int proNum;
    private int solved;
    private int penalty;
    private Date time;
    private List<ContestProblemRecord> contestProblemRecords;

    public int getContestId() {
        return contestId;
    }

    public int getProNum() {
        return proNum;
    }

    public String getTitle() {
        return title;
    }

    public void setContestId(int contestId) {
        this.contestId = contestId;
    }

    public void setProNum(int proNum) {
        this.proNum = proNum;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<ContestProblemRecord> getContestProblemRecords() {
        return contestProblemRecords;
    }

    public void setContestProblemRecords(List<ContestProblemRecord> contestProblemRecords) {
        this.contestProblemRecords = contestProblemRecords;
    }

    public int getSolved() {
        return solved;
    }

    public void setSolved(int solved) {
        this.solved = solved;
    }

    public int getPenalty() {
        return penalty;
    }

    public void setPenalty(int penalty) {
        this.penalty = penalty;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    @Override
    public int compareTo(PersonalContestLine personalContestLine) {
        if(this.getTime().before(personalContestLine.getTime())) {
            return 1;
        }
        else return -1;
    }
}
