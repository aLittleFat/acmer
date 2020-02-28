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
    private String title;

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

    @Basic
    @Column(name = "title")
    public String getTitle() { return title; }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Problem problem = (Problem) o;
        return id == problem.id &&
                Objects.equals(problemId, problem.problemId) &&
                Objects.equals(ojName, problem.ojName) &&
                Objects.equals(title, problem.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, problemId, ojName, title);
    }
}
