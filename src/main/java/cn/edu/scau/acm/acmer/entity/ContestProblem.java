package cn.edu.scau.acm.acmer.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class ContestProblem {
    private int id;
    private String iDinContest;
    private Integer contestId;
    private Integer problemId;

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "IDinContest")
    public String getiDinContest() {
        return iDinContest;
    }

    public void setiDinContest(String iDinContest) {
        this.iDinContest = iDinContest;
    }

    @Basic
    @Column(name = "ContestID")
    public Integer getContestId() {
        return contestId;
    }

    public void setContestId(Integer contestId) {
        this.contestId = contestId;
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
        ContestProblem that = (ContestProblem) o;
        return id == that.id &&
                Objects.equals(iDinContest, that.iDinContest) &&
                Objects.equals(contestId, that.contestId) &&
                Objects.equals(problemId, that.problemId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, iDinContest, contestId, problemId);
    }
}
