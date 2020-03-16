package cn.edu.scau.acm.acmer.entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "award_view", schema = "scauacm", catalog = "")
public class AwardView {
    private int id;
    private String level;
    private String regional;
    private Timestamp time;
    private String contestType;
    private byte verified;
    private int teamId;
    private String students;

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
    @Column(name = "level")
    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    @Basic
    @Column(name = "regional")
    public String getRegional() {
        return regional;
    }

    public void setRegional(String regional) {
        this.regional = regional;
    }

    @Basic
    @Column(name = "time")
    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    @Basic
    @Column(name = "contest_type")
    public String getContestType() {
        return contestType;
    }

    public void setContestType(String contestType) {
        this.contestType = contestType;
    }

    @Basic
    @Column(name = "verified")
    public byte getVerified() {
        return verified;
    }

    public void setVerified(byte verified) {
        this.verified = verified;
    }

    @Basic
    @Column(name = "team_id")
    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    @Basic
    @Column(name = "students")
    public String getStudents() {
        return students;
    }

    public void setStudents(String students) {
        this.students = students;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AwardView awardView = (AwardView) o;
        return id == awardView.id &&
                verified == awardView.verified &&
                teamId == awardView.teamId &&
                Objects.equals(level, awardView.level) &&
                Objects.equals(regional, awardView.regional) &&
                Objects.equals(time, awardView.time) &&
                Objects.equals(contestType, awardView.contestType) &&
                Objects.equals(students, awardView.students);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, level, regional, time, contestType, verified, teamId, students);
    }
}
