package cn.edu.scau.acm.acmer.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "personal_season_student", uniqueConstraints = {
        @UniqueConstraint(name = "uni", columnNames = {"student_id", "season_id"})
})
public class PersonalSeasonStudent {
    private int seasonId;
    private String studentId;
    private int id;

    @Basic
    @Column(name = "season_id")
    public int getSeasonId() {
        return seasonId;
    }

    public void setSeasonId(int seasonId) {
        this.seasonId = seasonId;
    }

    @Basic
    @Column(name = "student_id")
    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PersonalSeasonStudent that = (PersonalSeasonStudent) o;
        return seasonId == that.seasonId &&
                id == that.id &&
                Objects.equals(studentId, that.studentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seasonId, studentId, id);
    }
}
