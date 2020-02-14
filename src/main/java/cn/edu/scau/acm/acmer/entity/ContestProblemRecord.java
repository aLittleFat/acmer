package cn.edu.scau.acm.acmer.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "contest_problem_record", uniqueConstraints = {
        @UniqueConstraint(name = "uni", columnNames = {"contest_problem_id", "contest_record_id"})
})
public class ContestProblemRecord {
    private int id;
    private Integer contestProblemId;
    private String status;
    private Integer penalty;
    private Integer tries;
    private Integer contestRecordId;

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
    @Column(name = "contest_problem_id")
    public Integer getContestProblemId() {
        return contestProblemId;
    }

    public void setContestProblemId(Integer contestProblemId) {
        this.contestProblemId = contestProblemId;
    }

    @Basic
    @Column(name = "status")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Basic
    @Column(name = "penalty")
    public Integer getPenalty() {
        return penalty;
    }

    public void setPenalty(Integer penalty) {
        this.penalty = penalty;
    }

    @Basic
    @Column(name = "tries")
    public Integer getTries() {
        return tries;
    }

    public void setTries(Integer tries) {
        this.tries = tries;
    }

    @Basic
    @Column(name = "contest_record_id")
    public Integer getContestRecordId() {
        return contestRecordId;
    }

    public void setContestRecordId(Integer contestRecordId) {
        this.contestRecordId = contestRecordId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContestProblemRecord that = (ContestProblemRecord) o;
        return id == that.id &&
                Objects.equals(contestProblemId, that.contestProblemId) &&
                Objects.equals(status, that.status) &&
                Objects.equals(penalty, that.penalty) &&
                Objects.equals(tries, that.tries) &&
                Objects.equals(contestRecordId, that.contestRecordId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, contestProblemId, status, penalty, tries, contestRecordId);
    }
}
