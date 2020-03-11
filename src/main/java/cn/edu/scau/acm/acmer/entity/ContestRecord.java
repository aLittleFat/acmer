package cn.edu.scau.acm.acmer.entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "contest_record", uniqueConstraints = {
        @UniqueConstraint(name = "uni", columnNames = {"student_id", "team_id", "contest_id"})
})
public class ContestRecord {
    private int id;
    private int contestId;
    private String studentId;
    private Integer teamId;
    private String account;
    private Timestamp time;
    private String solution;
    private String solved;
    private String upSolved;
    private int penalty;

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
    @Column(name = "contest_id")
    public int getContestId() {
        return contestId;
    }

    public void setContestId(int contestId) {
        this.contestId = contestId;
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
    @Column(name = "team_id")
    public Integer getTeamId() {
        return teamId;
    }

    public void setTeamId(Integer teamId) {
        this.teamId = teamId;
    }

    @Basic
    @Column(name = "account")
    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    @Basic
    @Column(name = "time")
    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    @Basic
    @Column(name = "solution")
    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    @Basic
    @Column(name = "solved")
    public String getSolved() {
        return solved;
    }

    public void setSolved(String solved) {
        this.solved = solved;
    }

    @Basic
    @Column(name = "up_solved")
    public String getUpSolved() {
        return upSolved;
    }

    public void setUpSolved(String upSolved) {
        this.upSolved = upSolved;
    }

    @Basic
    @Column(name = "penalty")
    public int getPenalty() {
        return penalty;
    }

    public void setPenalty(int penalty) {
        this.penalty = penalty;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContestRecord that = (ContestRecord) o;
        return id == that.id &&
                contestId == that.contestId &&
                penalty == that.penalty &&
                Objects.equals(studentId, that.studentId) &&
                Objects.equals(teamId, that.teamId) &&
                Objects.equals(account, that.account) &&
                Objects.equals(time, that.time) &&
                Objects.equals(solution, that.solution) &&
                Objects.equals(solved, that.solved) &&
                Objects.equals(upSolved, that.upSolved);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, contestId, studentId, teamId, account, time, solution, solved, upSolved, penalty);
    }
}
