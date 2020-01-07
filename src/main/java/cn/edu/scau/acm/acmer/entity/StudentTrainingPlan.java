package cn.edu.scau.acm.acmer.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "Student_TrainingPlan", schema = "scauacm", catalog = "")
@IdClass(StudentTrainingPlanPK.class)
public class StudentTrainingPlan {
    private String studentId;
    private int trainingPlanId;

    @Id
    @Column(name = "StudentID")
    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    @Id
    @Column(name = "TrainingPlanID")
    public int getTrainingPlanId() {
        return trainingPlanId;
    }

    public void setTrainingPlanId(int trainingPlanId) {
        this.trainingPlanId = trainingPlanId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StudentTrainingPlan that = (StudentTrainingPlan) o;
        return trainingPlanId == that.trainingPlanId &&
                Objects.equals(studentId, that.studentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentId, trainingPlanId);
    }
}
