package cn.edu.scau.acm.acmer.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "contest_problem_record", uniqueConstraints = {
        @UniqueConstraint(name = "uni", columnNames = {"problem_index", "contest_record_id"})
})
public class ContestProblemRecord {
    private int id;
    private String status;
    private int penalty;
    private int tries;
    private int contestRecordId;
    private String problemIndex;

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
    @Column(name = "status")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Basic
    @Column(name = "penalty")
    public int getPenalty() {
        return penalty;
    }

    public void setPenalty(int penalty) {
        this.penalty = penalty;
    }

    @Basic
    @Column(name = "tries")
    public int getTries() {
        return tries;
    }

    public void setTries(int tries) {
        this.tries = tries;
    }

    @Basic
    @Column(name = "contest_record_id")
    public int getContestRecordId() {
        return contestRecordId;
    }

    public void setContestRecordId(int contestRecordId) {
        this.contestRecordId = contestRecordId;
    }

    @Basic
    @Column(name = "problem_index")
    public String getProblemIndex() {
        return problemIndex;
    }

    public void setProblemIndex(String problemIndex) {
        this.problemIndex = problemIndex;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContestProblemRecord that = (ContestProblemRecord) o;
        return id == that.id &&
                penalty == that.penalty &&
                tries == that.tries &&
                contestRecordId == that.contestRecordId &&
                Objects.equals(status, that.status) &&
                Objects.equals(problemIndex, that.problemIndex);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, status, penalty, tries, contestRecordId, problemIndex);
    }
}
