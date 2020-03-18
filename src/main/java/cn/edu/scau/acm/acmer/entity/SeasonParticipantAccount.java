package cn.edu.scau.acm.acmer.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "season_participant_account", uniqueConstraints = {
        @UniqueConstraint(name = "uni", columnNames = {"season_account_id", "team_id", "season_student_id"})
})
public class SeasonParticipantAccount {
    private int id;
    private int seasonAccountId;
    private Integer seasonStudentId;
    private Integer teamId;
    private String handle;
    private String account;
    private String password;

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
    @Column(name = "season_account_id")
    public int getSeasonAccountId() {
        return seasonAccountId;
    }

    public void setSeasonAccountId(int seasonAccountId) {
        this.seasonAccountId = seasonAccountId;
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
    @Column(name = "team_id")
    public Integer getTeamId() {
        return teamId;
    }

    public void setTeamId(Integer teamId) {
        this.teamId = teamId;
    }

    @Basic
    @Column(name = "handle")
    public String getHandle() {
        return handle;
    }

    public void setHandle(String handle) {
        this.handle = handle;
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
    @Column(name = "password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SeasonParticipantAccount that = (SeasonParticipantAccount) o;
        return id == that.id &&
                seasonAccountId == that.seasonAccountId &&
                Objects.equals(seasonStudentId, that.seasonStudentId) &&
                Objects.equals(teamId, that.teamId) &&
                Objects.equals(handle, that.handle) &&
                Objects.equals(account, that.account) &&
                Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, seasonAccountId, seasonStudentId, teamId, handle, account, password);
    }
}
