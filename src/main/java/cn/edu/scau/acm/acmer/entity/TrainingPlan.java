package cn.edu.scau.acm.acmer.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
public class TrainingPlan {
    private int id;
    private Timestamp time;
    private Timestamp endTime;
    private Integer contestId;

    @Id
    @Column(name = "ID")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "Time")
    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    @Basic
    @Column(name = "EndTime")
    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    @Basic
    @Column(name = "ContestID")
    public Integer getContestId() {
        return contestId;
    }

    public void setContestId(Integer contestId) {
        this.contestId = contestId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TrainingPlan that = (TrainingPlan) o;
        return id == that.id &&
                Objects.equals(time, that.time) &&
                Objects.equals(endTime, that.endTime) &&
                Objects.equals(contestId, that.contestId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, time, endTime, contestId);
    }
}
