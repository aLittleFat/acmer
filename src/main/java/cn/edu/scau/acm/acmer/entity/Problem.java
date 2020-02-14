package cn.edu.scau.acm.acmer.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "problem", uniqueConstraints = {
        @UniqueConstraint(name = "uni", columnNames = {"problem_id", "oj_name"})
})
public class Problem {
    private int id;
    private String problemId;
    private String ojName;

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
    @Column(name = "problem_id")
    public String getProblemId() {
        return problemId;
    }

    public void setProblemId(String problemId) {
        this.problemId = problemId;
    }

    @Basic
    @Column(name = "oj_name")
    public String getOjName() {
        return ojName;
    }

    public void setOjName(String ojName) {
        this.ojName = ojName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Problem problem = (Problem) o;
        return id == problem.id &&
                Objects.equals(problemId, problem.problemId) &&
                Objects.equals(ojName, problem.ojName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, problemId, ojName);
    }
}
