package cn.edu.scau.acm.acmer.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class StudentTrainingPlanPK implements Serializable {
    private String studentId;
    private int trainingPlanId;

    @Column(name = "StudentID")
    @Id
    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    @Column(name = "TrainingPlanID")
    @Id
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
        StudentTrainingPlanPK that = (StudentTrainingPlanPK) o;
        return trainingPlanId == that.trainingPlanId &&
                Objects.equals(studentId, that.studentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentId, trainingPlanId);
    }
}
