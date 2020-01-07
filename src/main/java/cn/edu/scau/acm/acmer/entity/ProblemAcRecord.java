package cn.edu.scau.acm.acmer.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
public class ProblemAcRecord {
    private int id;
    private Timestamp time;
    private Integer ojAccountId;
    private Integer problemId;

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
    @Column(name = "OJAccountID")
    public Integer getOjAccountId() {
        return ojAccountId;
    }

    public void setOjAccountId(Integer ojAccountId) {
        this.ojAccountId = ojAccountId;
    }

    @Basic
    @Column(name = "ProblemID")
    public Integer getProblemId() {
        return problemId;
    }

    public void setProblemId(Integer problemId) {
        this.problemId = problemId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProblemAcRecord that = (ProblemAcRecord) o;
        return id == that.id &&
                Objects.equals(time, that.time) &&
                Objects.equals(ojAccountId, that.ojAccountId) &&
                Objects.equals(problemId, that.problemId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, time, ojAccountId, problemId);
    }
}
