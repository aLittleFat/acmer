package cn.edu.scau.acm.acmer.entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "training_record_view", schema = "scauacm", catalog = "")
public class TrainingRecordView {
    private int trainingId;
    private int id;
    private int trainingParticipantId;
    private int contestId;
    private String title;
    private String problemList;
    private String ojName;
    private Timestamp time;
    private Integer penalty;
    private Long solvedNumber;
    private Long upSolvedNumber;
    private String solved;
    private String upSolved;
    private String solution;
    private String account;
    private String studentId;
    private Integer teamId;
    private String students;

    @Basic
    @Column(name = "training_id")
    public int getTrainingId() {
        return trainingId;
    }

    public void setTrainingId(int trainingId) {
        this.trainingId = trainingId;
    }

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
    @Column(name = "training_participant_id")
    public int getTrainingParticipantId() {
        return trainingParticipantId;
    }

    public void setTrainingParticipantId(int trainingParticipantId) {
        this.trainingParticipantId = trainingParticipantId;
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
    @Column(name = "title")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Basic
    @Column(name = "problem_list")
    public String getProblemList() {
        return problemList;
    }

    public void setProblemList(String problemList) {
        this.problemList = problemList;
    }

    @Basic
    @Column(name = "oj_name")
    public String getOjName() {
        return ojName;
    }

    public void setOjName(String ojName) {
        this.ojName = ojName;
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
    @Column(name = "penalty")
    public Integer getPenalty() {
        return penalty;
    }

    public void setPenalty(int penalty) {
        this.penalty = penalty;
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
    @Column(name = "up_solved_number")
    public Long getUpSolvedNumber() {
        return upSolvedNumber;
    }

    public void setUpSolvedNumber(Long upSolvedNumber) {
        this.upSolvedNumber = upSolvedNumber;
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
    @Column(name = "solution")
    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
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
        TrainingRecordView that = (TrainingRecordView) o;
        return trainingId == that.trainingId &&
                id == that.id &&
                trainingParticipantId == that.trainingParticipantId &&
                contestId == that.contestId &&
                penalty == that.penalty &&
                Objects.equals(title, that.title) &&
                Objects.equals(problemList, that.problemList) &&
                Objects.equals(ojName, that.ojName) &&
                Objects.equals(time, that.time) &&
                Objects.equals(solvedNumber, that.solvedNumber) &&
                Objects.equals(upSolvedNumber, that.upSolvedNumber) &&
                Objects.equals(solved, that.solved) &&
                Objects.equals(upSolved, that.upSolved) &&
                Objects.equals(solution, that.solution) &&
                Objects.equals(account, that.account) &&
                Objects.equals(studentId, that.studentId) &&
                Objects.equals(teamId, that.teamId) &&
                Objects.equals(students, that.students);
    }

    @Override
    public int hashCode() {
        return Objects.hash(trainingId, id, trainingParticipantId, contestId, title, problemList, ojName, time, penalty, solvedNumber, upSolvedNumber, solved, upSolved, solution, account, studentId, teamId, students);
    }
}
