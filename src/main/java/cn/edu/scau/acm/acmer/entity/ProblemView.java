package cn.edu.scau.acm.acmer.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "problem_view", schema = "scauacm", catalog = "")
public class ProblemView {
    private int id;
    private String problemId;
    private String ojName;
    private String title;
    private BigDecimal difficult;
    private String tags;

    @Basic
    @Column(name = "id")
    @Id
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
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Basic
    @Column(name = "difficult")
    public BigDecimal getDifficult() {
        return difficult;
    }

    public void setDifficult(BigDecimal difficult) {
        this.difficult = difficult;
    }

    @Basic
    @Column(name = "tags")
    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProblemView that = (ProblemView) o;
        return id == that.id &&
                Objects.equals(problemId, that.problemId) &&
                Objects.equals(ojName, that.ojName) &&
                Objects.equals(title, that.title) &&
                Objects.equals(difficult, that.difficult) &&
                Objects.equals(tags, that.tags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, problemId, ojName, title, difficult, tags);
    }
}
