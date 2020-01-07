package cn.edu.scau.acm.acmer.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class TeamTrainingPlanPK implements Serializable {
    private int teamId;
    private int trainingPlanId;

    @Column(name = "TeamID")
    @Id
    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
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
        TeamTrainingPlanPK that = (TeamTrainingPlanPK) o;
        return teamId == that.teamId &&
                trainingPlanId == that.trainingPlanId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(teamId, trainingPlanId);
    }
}
