package cn.edu.scau.acm.acmer.model;

import cn.edu.scau.acm.acmer.entity.Contest;
import cn.edu.scau.acm.acmer.entity.PersonalContestProblemRecord;
import cn.edu.scau.acm.acmer.entity.PersonalContestRecord;

import java.util.List;

public class PersonalContestRank implements Comparable<PersonalContestRank> {
    private Contest contest;
    private int solved;
    private int penalty;
    private PersonalContestRecord personalContestRecord;
    private List<PersonalContestProblemRecord> personalContestProblemRecords;

    public Contest getContest() {
        return contest;
    }

    public List<PersonalContestProblemRecord> getPersonalContestProblemRecords() {
        return personalContestProblemRecords;
    }

    public PersonalContestRecord getPersonalContestRecord() {
        return personalContestRecord;
    }

    public void setContest(Contest contest) {
        this.contest = contest;
    }

    public void setPersonalContestProblemRecords(List<PersonalContestProblemRecord> personalContestProblemRecords) {
        this.personalContestProblemRecords = personalContestProblemRecords;
    }

    public void setPersonalContestRecord(PersonalContestRecord personalContestRecord) {
        this.personalContestRecord = personalContestRecord;
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

    @Override
    public int compareTo(PersonalContestRank personalContestRank) {
        if(this.getPersonalContestRecord().getTime().before(personalContestRank.getPersonalContestRecord().getTime())) {
            return 1;
        }
        else return -1;
    }
}
