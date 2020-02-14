package cn.edu.scau.acm.acmer.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "training_plan_participant", uniqueConstraints = {
        @UniqueConstraint(name = "uni", columnNames = {"student_id", "traning_plan_id", "student_id"})
})
public class TrainingPlanParticipant {
    private int id;
    private Integer traningPlanId;
    private Integer teamId;
    private String studentId;

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
    @Column(name = "traning_plan_id")
    public Integer getTraningPlanId() {
        return traningPlanId;
    }

    public void setTraningPlanId(Integer traningPlanId) {
        this.traningPlanId = traningPlanId;
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
    @Column(name = "student_id")
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
        TrainingPlanParticipant that = (TrainingPlanParticipant) o;
        return id == that.id &&
                Objects.equals(traningPlanId, that.traningPlanId) &&
                Objects.equals(teamId, that.teamId) &&
                Objects.equals(studentId, that.studentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, traningPlanId, teamId, studentId);
    }
}
