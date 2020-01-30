package cn.edu.scau.acm.acmer.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Season {
    private int id;
    private String name;
    private String type;
    private Double cfProportion;

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
    @Column(name = "Type")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Basic
    @Column(name = "CFProportion")
    public Double getCfProportion() {
        return cfProportion;
    }

    public void setCfProportion(Double cfProportion) {
        this.cfProportion = cfProportion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Season season = (Season) o;
        return id == season.id &&
                Objects.equals(name, season.name) &&
                Objects.equals(type, season.type) &&
                Objects.equals(cfProportion, season.cfProportion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, type, cfProportion);
    }
}
