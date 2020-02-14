package cn.edu.scau.acm.acmer.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "team", uniqueConstraints = {
        @UniqueConstraint(name = "uni", columnNames = {"rank", "season_id"})
})
public class Team {
    private int id;
    private String nameCn;
    private String nameEn;
    private Integer rank;
    private Integer seasonId;

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
    @Column(name = "rank")
    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    @Basic
    @Column(name = "season_id")
    public Integer getSeasonId() {
        return seasonId;
    }

    public void setSeasonId(Integer seasonId) {
        this.seasonId = seasonId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Team team = (Team) o;
        return id == team.id &&
                Objects.equals(nameCn, team.nameCn) &&
                Objects.equals(nameEn, team.nameEn) &&
                Objects.equals(rank, team.rank) &&
                Objects.equals(seasonId, team.seasonId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nameCn, nameEn, rank, seasonId);
    }
}
