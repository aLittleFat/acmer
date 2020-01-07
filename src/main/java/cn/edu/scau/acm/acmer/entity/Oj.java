package cn.edu.scau.acm.acmer.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class Oj {
    private String name;

    @Id
    @Column(name = "Name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Oj oj = (Oj) o;
        return Objects.equals(name, oj.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
