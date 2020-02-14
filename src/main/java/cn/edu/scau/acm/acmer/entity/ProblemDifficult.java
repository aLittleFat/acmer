package cn.edu.scau.acm.acmer.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "problem_difficult", uniqueConstraints = {
        @UniqueConstraint(name = "uni", columnNames = {"student_id", "problem_id"})
})
public class ProblemDifficult {
    private int id;
    private BigDecimal difficult;
    private String studentId;
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
    @Column(name = "difficult")
    public BigDecimal getDifficult() {
        return difficult;
    }

    public void setDifficult(BigDecimal difficult) {
        this.difficult = difficult;
    }

    @Basic
    @Column(name = "student_id")
    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
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
        ProblemDifficult that = (ProblemDifficult) o;
        return id == that.id &&
                Objects.equals(difficult, that.difficult) &&
                Objects.equals(studentId, that.studentId) &&
                Objects.equals(problemId, that.problemId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, difficult, studentId, problemId);
    }
}
