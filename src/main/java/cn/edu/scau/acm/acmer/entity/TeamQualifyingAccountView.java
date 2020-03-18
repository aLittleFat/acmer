package cn.edu.scau.acm.acmer.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "team_qualifying_account_view", schema = "scauacm", catalog = "")
public class TeamQualifyingAccountView {
    private int qualifyingId;
    private int teamId;
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
    @Column(name = "team_id")
    @Id
    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
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
        TeamQualifyingAccountView that = (TeamQualifyingAccountView) o;
        return qualifyingId == that.qualifyingId &&
                teamId == that.teamId &&
                contestId == that.contestId &&
                Objects.equals(account, that.account);
    }

    @Override
    public int hashCode() {
        return Objects.hash(qualifyingId, teamId, contestId, account);
    }
}
