package cn.edu.scau.acm.acmer.entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "Contest", uniqueConstraints = {
        @UniqueConstraint(name = "uni", columnNames = {"oj_name", "cid"})
})
public class Contest {
    private int id;
    private Timestamp startTime;
    private Timestamp endTime;
    private String cid;
    private String title;
    private String password;
    private String ojName;
    private String username;
    private Integer problemNumber;

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
    @Column(name = "start_time")
    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    @Basic
    @Column(name = "end_time")
    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    @Basic
    @Column(name = "cid")
    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
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
    @Column(name = "password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
    @Column(name = "username")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Basic
    @Column(name = "problem_number")
    public Integer getProblemNumber() {
        return problemNumber;
    }

    public void setProblemNumber(Integer problemNumber) {
        this.problemNumber = problemNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contest contest = (Contest) o;
        return id == contest.id &&
                Objects.equals(startTime, contest.startTime) &&
                Objects.equals(endTime, contest.endTime) &&
                Objects.equals(cid, contest.cid) &&
                Objects.equals(title, contest.title) &&
                Objects.equals(password, contest.password) &&
                Objects.equals(ojName, contest.ojName) &&
                Objects.equals(username, contest.username) &&
                Objects.equals(problemNumber, contest.problemNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, startTime, endTime, cid, title, password, ojName, username, problemNumber);
    }
}
