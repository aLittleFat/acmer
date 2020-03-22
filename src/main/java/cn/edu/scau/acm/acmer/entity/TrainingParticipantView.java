package cn.edu.scau.acm.acmer.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "training_participant_view")
public class TrainingParticipantView {
    private int id;
    private int trainingId;
    private Integer teamId;
    private String studentId;
    private String students;

    @Basic
    @Column(name = "id")
    @Id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "training_id")
    public int getTrainingId() {
        return trainingId;
    }

    public void setTrainingId(int trainingId) {
        this.trainingId = trainingId;
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

    @Basic
    @Column(name = "students")
    public String getStudents() {
        return students;
    }

    public void setStudents(String students) {
        this.students = students;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TrainingParticipantView that = (TrainingParticipantView) o;
        return id == that.id &&
                trainingId == that.trainingId &&
                Objects.equals(teamId, that.teamId) &&
                Objects.equals(studentId, that.studentId) &&
                Objects.equals(students, that.students);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, trainingId, teamId, studentId, students);
    }
}
