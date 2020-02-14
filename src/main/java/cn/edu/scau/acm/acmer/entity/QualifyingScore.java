package cn.edu.scau.acm.acmer.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "qualifying_score", uniqueConstraints = {
    @UniqueConstraint(name = "uni", columnNames = {"qualifying_id", "team_id", "personal_season_student_id"})
})
public class QualifyingScore {
    private int id;
    private Double score;
    private Integer qualifyingId;
    private Integer teamId;
    private Integer personalSeasonStudentId;

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
    @Column(name = "score")
    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    @Basic
    @Column(name = "qualifying_id")
    public Integer getQualifyingId() {
        return qualifyingId;
    }

    public void setQualifyingId(Integer qualifyingId) {
        this.qualifyingId = qualifyingId;
    }

    @Basic
    @Column(name = "team_id")
    public Integer getTeamId() {
        return teamId;
    }

    public void setTeamId(Integer teamId) {
        this.teamId = teamId;
    }

    @Basic
    @Column(name = "personal_season_student_id")
    public Integer getPersonalSeasonStudentId() {
        return personalSeasonStudentId;
    }

    public void setPersonalSeasonStudentId(Integer personalSeasonStudentId) {
        this.personalSeasonStudentId = personalSeasonStudentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QualifyingScore that = (QualifyingScore) o;
        return id == that.id &&
                Objects.equals(score, that.score) &&
                Objects.equals(qualifyingId, that.qualifyingId) &&
                Objects.equals(teamId, that.teamId) &&
                Objects.equals(personalSeasonStudentId, that.personalSeasonStudentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, score, qualifyingId, teamId, personalSeasonStudentId);
    }
}
