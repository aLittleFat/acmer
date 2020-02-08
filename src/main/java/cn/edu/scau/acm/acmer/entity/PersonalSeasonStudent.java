package cn.edu.scau.acm.acmer.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "PersonalSeasonStudent", uniqueConstraints = {
        @UniqueConstraint(name = "uni", columnNames = {"SeasonID", "StudentID"})
})
public class PersonalSeasonStudent {
    private int seasonId;
    private String studentId;
    private int id;

    @Basic
    @Column(name = "SeasonID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getSeasonId() {
        return seasonId;
    }

    public void setSeasonId(int seasonId) {
        this.seasonId = seasonId;
    }

    @Basic
    @Column(name = "StudentID")
    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    @Id
    @Column(name = "ID")
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
