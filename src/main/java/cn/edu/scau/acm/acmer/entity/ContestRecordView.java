package cn.edu.scau.acm.acmer.entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "contest_record_view", schema = "scauacm", catalog = "")
public class ContestRecordView {
    private int id;
    private int contestId;
    private String title;
    private String problemList;
    private Timestamp time;
    private int penalty;
    private String solved;
    private String upSolved;
    private String studentId;
    private Integer teamId;
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
    @Column(name = "time")
    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
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
        ContestRecordView that = (ContestRecordView) o;
        return id == that.id &&
                contestId == that.contestId &&
                penalty == that.penalty &&
                Objects.equals(title, that.title) &&
                Objects.equals(problemList, that.problemList) &&
                Objects.equals(time, that.time) &&
                Objects.equals(solved, that.solved) &&
                Objects.equals(upSolved, that.upSolved) &&
                Objects.equals(studentId, that.studentId) &&
                Objects.equals(teamId, that.teamId) &&
                Objects.equals(students, that.students);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, contestId, title, problemList, time, penalty, solved, upSolved, studentId, teamId, students);
    }
}
