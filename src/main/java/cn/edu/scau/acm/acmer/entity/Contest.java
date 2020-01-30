package cn.edu.scau.acm.acmer.entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
public class Contest {
    private int id;
    private Timestamp startTime;
    private Timestamp endTime;
    private String ojid;
    private String name;
    private String password;
    private String ojName;

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
    @Column(name = "StartTime")
    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
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
    @Column(name = "OJID")
    public String getOjid() {
        return ojid;
    }

    public void setOjid(String ojid) {
        this.ojid = ojid;
    }

    @Basic
    @Column(name = "Name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "Password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Basic
    @Column(name = "OJName")
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
        Contest contest = (Contest) o;
        return id == contest.id &&
                Objects.equals(startTime, contest.startTime) &&
                Objects.equals(endTime, contest.endTime) &&
                Objects.equals(ojid, contest.ojid) &&
                Objects.equals(name, contest.name) &&
                Objects.equals(password, contest.password) &&
                Objects.equals(ojName, contest.ojName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, startTime, endTime, ojid, name, password, ojName);
    }
}
