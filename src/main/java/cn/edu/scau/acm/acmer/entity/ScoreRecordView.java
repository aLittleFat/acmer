package cn.edu.scau.acm.acmer.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "score_record_view", schema = "scauacm", catalog = "")
public class ScoreRecordView {
    private int id;
    private double score;
    private int qualifyingId;
    private Integer teamId;
    private Integer seasonStudentId;
    private Integer contestRecordId;
    private String students;
    private Long solvedNumber;
    private Integer penalty;
    private String solved;
    private Long upSolvedNumber;
    private String upSolved;
    private String solution;

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
    @Column(name = "score")
    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    @Basic
    @Column(name = "qualifying_id")
    public int getQualifyingId() {
        return qualifyingId;
    }

    public void setQualifyingId(int qualifyingId) {
        this.qualifyingId = qualifyingId;
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
    @Column(name = "season_student_id")
    public Integer getSeasonStudentId() {
        return seasonStudentId;
    }

    public void setSeasonStudentId(Integer seasonStudentId) {
        this.seasonStudentId = seasonStudentId;
    }

    @Basic
    @Column(name = "contest_record_id")
    public Integer getContestRecordId() {
        return contestRecordId;
    }

    public void setContestRecordId(Integer contestRecordId) {
        this.contestRecordId = contestRecordId;
    }

    @Basic
    @Column(name = "students")
    public String getStudents() {
        return students;
    }

    public void setStudents(String students) {
        this.students = students;
    }

    @Basic
    @Column(name = "solved_number")
    public Long getSolvedNumber() {
        return solvedNumber;
    }

    public void setSolvedNumber(Long solvedNumber) {
        this.solvedNumber = solvedNumber;
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
    @Column(name = "solved")
    public String getSolved() {
        return solved;
    }

    public void setSolved(String solved) {
        this.solved = solved;
    }

    @Basic
    @Column(name = "up_solved_number")
    public Long getUpSolvedNumber() {
        return upSolvedNumber;
    }

    public void setUpSolvedNumber(Long upSolvedNumber) {
        this.upSolvedNumber = upSolvedNumber;
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
    @Column(name = "solution")
    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScoreRecordView that = (ScoreRecordView) o;
        return id == that.id &&
                Double.compare(that.score, score) == 0 &&
                qualifyingId == that.qualifyingId &&
                Objects.equals(teamId, that.teamId) &&
                Objects.equals(seasonStudentId, that.seasonStudentId) &&
                Objects.equals(contestRecordId, that.contestRecordId) &&
                Objects.equals(students, that.students) &&
                Objects.equals(solvedNumber, that.solvedNumber) &&
                Objects.equals(penalty, that.penalty) &&
                Objects.equals(solved, that.solved) &&
                Objects.equals(upSolvedNumber, that.upSolvedNumber) &&
                Objects.equals(upSolved, that.upSolved) &&
                Objects.equals(solution, that.solution);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, score, qualifyingId, teamId, seasonStudentId, contestRecordId, students, solvedNumber, penalty, solved, upSolvedNumber, upSolved, solution);
    }
}
