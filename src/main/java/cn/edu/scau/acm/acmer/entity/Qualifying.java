package cn.edu.scau.acm.acmer.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Qualifying {
    private int id;
    private String title;
    private double proportion;
    private int seasonId;
    private int contestId;
    private Integer seasonAccountId;
    private byte calculated;

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
    @Column(name = "title")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Basic
    @Column(name = "proportion")
    public double getProportion() {
        return proportion;
    }

    public void setProportion(double proportion) {
        this.proportion = proportion;
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
    @Column(name = "contest_id")
    public int getContestId() {
        return contestId;
    }

    public void setContestId(int contestId) {
        this.contestId = contestId;
    }

    @Basic
    @Column(name = "season_account_id")
    public Integer getSeasonAccountId() {
        return seasonAccountId;
    }

    public void setSeasonAccountId(Integer seasonAccountId) {
        this.seasonAccountId = seasonAccountId;
    }

    @Basic
    @Column(name = "calculated")
    public byte getCalculated() {
        return calculated;
    }

    public void setCalculated(byte calculated) {
        this.calculated = calculated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Qualifying that = (Qualifying) o;
        return id == that.id &&
                Double.compare(that.proportion, proportion) == 0 &&
                seasonId == that.seasonId &&
                contestId == that.contestId &&
                calculated == that.calculated &&
                Objects.equals(title, that.title) &&
                Objects.equals(seasonAccountId, that.seasonAccountId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, proportion, seasonId, contestId, seasonAccountId, calculated);
    }
}
