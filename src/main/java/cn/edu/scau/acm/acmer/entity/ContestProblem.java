package cn.edu.scau.acm.acmer.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "contest_problem", uniqueConstraints = {
        @UniqueConstraint(name = "uni", columnNames = {"problem_index", "contest_id"})
})
public class ContestProblem {
    private int id;
    private String problemIndex;
    private Integer contestId;
    private Integer problemId;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "problem_index")
    public String getProblemIndex() {
        return problemIndex;
    }

    public void setProblemIndex(String problemIndex) {
        this.problemIndex = problemIndex;
    }

    @Basic
    @Column(name = "contest_id")
    public Integer getContestId() {
        return contestId;
    }

    public void setContestId(Integer contestId) {
        this.contestId = contestId;
    }

    @Basic
    @Column(name = "problem_id")
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
                Objects.equals(problemIndex, that.problemIndex) &&
                Objects.equals(contestId, that.contestId) &&
                Objects.equals(problemId, that.problemId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, problemIndex, contestId, problemId);
    }
}
