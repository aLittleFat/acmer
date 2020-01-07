package cn.edu.scau.acm.acmer.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
public class TeamAward {
    private int id;
    private String award;
    private String regional;
    private Timestamp time;
    private String contestType;
    private Byte verify;
    private Integer teamId;

    @Id
    @Column(name = "ID")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "Award")
    public String getAward() {
        return award;
    }

    public void setAward(String award) {
        this.award = award;
    }

    @Basic
    @Column(name = "Regional")
    public String getRegional() {
        return regional;
    }

    public void setRegional(String regional) {
        this.regional = regional;
    }

    @Basic
    @Column(name = "Time")
    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    @Basic
    @Column(name = "ContestType")
    public String getContestType() {
        return contestType;
    }

    public void setContestType(String contestType) {
        this.contestType = contestType;
    }

    @Basic
    @Column(name = "Verify")
    public Byte getVerify() {
        return verify;
    }

    public void setVerify(Byte verify) {
        this.verify = verify;
    }

    @Basic
    @Column(name = "TeamID")
    public Integer getTeamId() {
        return teamId;
    }

    public void setTeamId(Integer teamId) {
        this.teamId = teamId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TeamAward teamAward = (TeamAward) o;
        return id == teamAward.id &&
                Objects.equals(award, teamAward.award) &&
                Objects.equals(regional, teamAward.regional) &&
                Objects.equals(time, teamAward.time) &&
                Objects.equals(contestType, teamAward.contestType) &&
                Objects.equals(verify, teamAward.verify) &&
                Objects.equals(teamId, teamAward.teamId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, award, regional, time, contestType, verify, teamId);
    }
}
