package cn.edu.scau.acm.acmer.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "personal_qualifying_account_view", schema = "scauacm", catalog = "")
public class PersonalQualifyingAccountView {
    private int qualifyingId;
    private int seasonStudentId;
    private String studentId;
    private int contestId;
    private String account;

    @Basic
    @Column(name = "qualifying_id")
    public int getQualifyingId() {
        return qualifyingId;
    }

    public void setQualifyingId(int qualifyingId) {
        this.qualifyingId = qualifyingId;
    }

    @Basic
    @Column(name = "season_student_id")
    @Id
    public int getSeasonStudentId() {
        return seasonStudentId;
    }

    public void setSeasonStudentId(int seasonStudentId) {
        this.seasonStudentId = seasonStudentId;
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
    @Column(name = "contest_id")
    public int getContestId() {
        return contestId;
    }

    public void setContestId(int contestId) {
        this.contestId = contestId;
    }

    @Basic
    @Column(name = "account")
    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PersonalQualifyingAccountView that = (PersonalQualifyingAccountView) o;
        return qualifyingId == that.qualifyingId &&
                seasonStudentId == that.seasonStudentId &&
                contestId == that.contestId &&
                Objects.equals(studentId, that.studentId) &&
                Objects.equals(account, that.account);
    }

    @Override
    public int hashCode() {
        return Objects.hash(qualifyingId, seasonStudentId, studentId, contestId, account);
    }
}
