package cn.edu.scau.acm.acmer.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class PersonalRankingScore {
    private int id;
    private Double score;
    private Integer personalSeasonStudentId;
    private Integer qualifyingId;

    @Id
    @Column(name = "ID")
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
    @Column(name = "PersonalSeasonStudentID")
    public Integer getPersonalSeasonStudentId() {
        return personalSeasonStudentId;
    }

    public void setPersonalSeasonStudentId(Integer personalSeasonStudentId) {
        this.personalSeasonStudentId = personalSeasonStudentId;
    }

    @Basic
    @Column(name = "QualifyingID")
    public Integer getQualifyingId() {
        return qualifyingId;
    }

    public void setQualifyingId(Integer qualifyingId) {
        this.qualifyingId = qualifyingId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PersonalRankingScore that = (PersonalRankingScore) o;
        return id == that.id &&
                Objects.equals(score, that.score) &&
                Objects.equals(personalSeasonStudentId, that.personalSeasonStudentId) &&
                Objects.equals(qualifyingId, that.qualifyingId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, score, personalSeasonStudentId, qualifyingId);
    }
}
