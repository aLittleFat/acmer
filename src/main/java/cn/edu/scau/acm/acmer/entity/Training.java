package cn.edu.scau.acm.acmer.entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "training")
public class Training {
    private int id;
    private Timestamp endTime;
    private int contestId;
    private String title;

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
    @Column(name = "end_time")
    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Training that = (Training) o;
        return id == that.id &&
                contestId == that.contestId &&
                Objects.equals(endTime, that.endTime) &&
                Objects.equals(title, that.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, endTime, contestId, title);
    }
}
