package cn.edu.scau.acm.acmer.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "TeamRankingScore", uniqueConstraints = {
        @UniqueConstraint(name = "uni", columnNames = {"TeamID", "QualifyingID"})
})
public class TeamRankingScore {
    private int id;
    private Double score;
    private Integer qualifyingId;
    private Integer teamId;

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
    @Column(name = "Score")
    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    @Basic
    @Column(name = "QualifyingID")
    public Integer getQualifyingId() {
        return qualifyingId;
    }

    public void setQualifyingId(Integer qualifyingId) {
        this.qualifyingId = qualifyingId;
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
        TeamRankingScore that = (TeamRankingScore) o;
        return id == that.id &&
                Objects.equals(score, that.score) &&
                Objects.equals(qualifyingId, that.qualifyingId) &&
                Objects.equals(teamId, that.teamId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, score, qualifyingId, teamId);
    }
}
