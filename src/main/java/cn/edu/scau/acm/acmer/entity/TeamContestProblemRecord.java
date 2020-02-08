package cn.edu.scau.acm.acmer.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "TeamContestProblemRecord", uniqueConstraints = {
        @UniqueConstraint(name = "uni", columnNames = {"ContestProblemID", "TeamContestRecordID"})
})
public class TeamContestProblemRecord {
    private int id;
    private String status;
    private Integer acTime;
    private Integer tries;
    private Integer contestProblemId;
    private Integer teamContestRecordId;

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
    @Column(name = "Status")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Basic
    @Column(name = "ACTime")
    public Integer getAcTime() {
        return acTime;
    }

    public void setAcTime(Integer acTime) {
        this.acTime = acTime;
    }

    @Basic
    @Column(name = "Tries")
    public Integer getTries() {
        return tries;
    }

    public void setTries(Integer tries) {
        this.tries = tries;
    }

    @Basic
    @Column(name = "ContestProblemID")
    public Integer getContestProblemId() {
        return contestProblemId;
    }

    public void setContestProblemId(Integer contestProblemId) {
        this.contestProblemId = contestProblemId;
    }

    @Basic
    @Column(name = "TeamContestRecordID")
    public Integer getTeamContestRecordId() {
        return teamContestRecordId;
    }

    public void setTeamContestRecordId(Integer teamContestRecordId) {
        this.teamContestRecordId = teamContestRecordId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TeamContestProblemRecord that = (TeamContestProblemRecord) o;
        return id == that.id &&
                Objects.equals(status, that.status) &&
                Objects.equals(acTime, that.acTime) &&
                Objects.equals(tries, that.tries) &&
                Objects.equals(contestProblemId, that.contestProblemId) &&
                Objects.equals(teamContestRecordId, that.teamContestRecordId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, status, acTime, tries, contestProblemId, teamContestRecordId);
    }
}
