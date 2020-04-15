package cn.edu.scau.acm.acmer.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "team", uniqueConstraints = {
        @UniqueConstraint(name = "uni", columnNames = {"rank_num", "season_id"})
})
public class Team {
    private int id;
    private String nameCn;
    private String nameEn;
    private int rankNum;
    private int seasonId;
    private String vjAccount;

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
    @Column(name = "name_cn")
    public String getNameCn() {
        return nameCn;
    }

    public void setNameCn(String nameCn) {
        this.nameCn = nameCn;
    }

    @Basic
    @Column(name = "name_en")
    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    @Basic
    @Column(name = "rank_num")
    public int getRankNum() {
        return rankNum;
    }

    public void setRankNum(int rankNum) {
        this.rankNum = rankNum;
    }

    @Basic
    @Column(name = "season_id")
    public int getSeasonId() {
        return seasonId;
    }

    public void setSeasonId(int seasonId) {
        this.seasonId = seasonId;
    }

    @Basic
    @Column(name = "vj_account")
    public String getVjAccount() {
        return vjAccount;
    }

    public void setVjAccount(String vjAccount) {
        this.vjAccount = vjAccount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Team team = (Team) o;
        return id == team.id &&
                rankNum == team.rankNum &&
                seasonId == team.seasonId &&
                Objects.equals(nameCn, team.nameCn) &&
                Objects.equals(nameEn, team.nameEn) &&
                Objects.equals(vjAccount, team.vjAccount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nameCn, nameEn, rankNum, seasonId, vjAccount);
    }
}
