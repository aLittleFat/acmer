package cn.edu.scau.acm.acmer.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Qualifying {
    private int id;
    private String name;
    private Double proportion;
    private Integer seasonId;
    private Integer contestId;

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "Name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "Proportion")
    public Double getProportion() {
        return proportion;
    }

    public void setProportion(Double proportion) {
        this.proportion = proportion;
    }

    @Basic
    @Column(name = "SeasonID")
    public Integer getSeasonId() {
        return seasonId;
    }

    public void setSeasonId(Integer seasonId) {
        this.seasonId = seasonId;
    }

    @Basic
    @Column(name = "ContestID")
    public Integer getContestId() {
        return contestId;
    }

    public void setContestId(Integer contestId) {
        this.contestId = contestId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Qualifying that = (Qualifying) o;
        return id == that.id &&
                Objects.equals(name, that.name) &&
                Objects.equals(proportion, that.proportion) &&
                Objects.equals(seasonId, that.seasonId) &&
                Objects.equals(contestId, that.contestId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, proportion, seasonId, contestId);
    }
}
