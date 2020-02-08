package cn.edu.scau.acm.acmer.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "Student", uniqueConstraints = {
        @UniqueConstraint(name = "uni", columnNames = {"UserID"})
})
public class Student {
    private String id;
    private Integer grade;
    private String icpcEmail;
    private String status;
    private Integer userId;

    @Id
    @Column(name = "ID")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "Grade")
    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    @Basic
    @Column(name = "ICPC_Email")
    public String getIcpcEmail() {
        return icpcEmail;
    }

    public void setIcpcEmail(String icpcEmail) {
        this.icpcEmail = icpcEmail;
    }

    @Basic
    @Column(name = "Status")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Basic
    @Column(name = "UserID")
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return Objects.equals(id, student.id) &&
                Objects.equals(grade, student.grade) &&
                Objects.equals(icpcEmail, student.icpcEmail) &&
                Objects.equals(status, student.status) &&
                Objects.equals(userId, student.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, grade, icpcEmail, status, userId);
    }
}
