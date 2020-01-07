package cn.edu.scau.acm.acmer.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "Team_TrainingPlan", schema = "scauacm", catalog = "")
@IdClass(TeamTrainingPlanPK.class)
public class TeamTrainingPlan {
    private int teamId;
    private int trainingPlanId;

    @Id
    @Column(name = "TeamID")
    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
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
        TeamTrainingPlan that = (TeamTrainingPlan) o;
        return teamId == that.teamId &&
                trainingPlanId == that.trainingPlanId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(teamId, trainingPlanId);
    }
}
