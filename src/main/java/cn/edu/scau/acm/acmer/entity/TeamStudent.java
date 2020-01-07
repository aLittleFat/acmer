package cn.edu.scau.acm.acmer.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "Team_Student", schema = "scauacm", catalog = "")
@IdClass(TeamStudentPK.class)
public class TeamStudent {
    private int teamId;
    private String studentId;

    @Id
    @Column(name = "TeamID")
    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    @Id
    @Column(name = "StudentID")
    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TeamStudent that = (TeamStudent) o;
        return teamId == that.teamId &&
                Objects.equals(studentId, that.studentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(teamId, studentId);
    }
}
