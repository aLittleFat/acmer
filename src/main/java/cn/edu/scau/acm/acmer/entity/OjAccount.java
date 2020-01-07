package cn.edu.scau.acm.acmer.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class OjAccount {
    private int id;
    private String account;
    private String studentId;
    private String ojName;

    @Id
    @Column(name = "ID")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "Account")
    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    @Basic
    @Column(name = "StudentID")
    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    @Basic
    @Column(name = "OJName")
    public String getOjName() {
        return ojName;
    }

    public void setOjName(String ojName) {
        this.ojName = ojName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OjAccount ojAccount = (OjAccount) o;
        return id == ojAccount.id &&
                Objects.equals(account, ojAccount.account) &&
                Objects.equals(studentId, ojAccount.studentId) &&
                Objects.equals(ojName, ojAccount.ojName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, account, studentId, ojName);
    }
}
