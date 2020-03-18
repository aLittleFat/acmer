package cn.edu.scau.acm.acmer.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "qualifying_score", uniqueConstraints = {
    @UniqueConstraint(name = "uni", columnNames = {"qualifying_id", "team_id", "season_student_id"})
})
public class QualifyingScore {
    private int id;
    private double score;
    private int qualifyingId;
    private Integer teamId;
    private Integer seasonStudentId;

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
    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    @Basic
    @Column(name = "qualifying_id")
    public int getQualifyingId() {
        return qualifyingId;
    }

    public void setQualifyingId(int qualifyingId) {
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
    @Column(name = "season_student_id")
    public Integer getSeasonStudentId() {
        return seasonStudentId;
    }

    public void setSeasonStudentId(Integer seasonStudentId) {
        this.seasonStudentId = seasonStudentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QualifyingScore that = (QualifyingScore) o;
        return id == that.id &&
                Double.compare(that.score, score) == 0 &&
                qualifyingId == that.qualifyingId &&
                Objects.equals(teamId, that.teamId) &&
                Objects.equals(seasonStudentId, that.seasonStudentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, score, qualifyingId, teamId, seasonStudentId);
    }
}
