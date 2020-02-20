package cn.edu.scau.acm.acmer.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "user", uniqueConstraints = {
        @UniqueConstraint(name = "email", columnNames = {"email"}),
        @UniqueConstraint(name = "studentId", columnNames = {"student_id"})
})
public class User {
    private int id;
    private String email;
    private String phone;
    private String name;
    private String password;
    private byte isAdmin;
    private byte verified;
    private String studentId;
    private Integer grade;
    private String icpcEmail;
    private String status;
    private String qq;

    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Basic
    @Column(name = "phone")
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Basic
    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Basic
    @Column(name = "is_admin")
    public byte getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(byte isAdmin) {
        this.isAdmin = isAdmin;
    }

    @Basic
    @Column(name = "verified")
    public byte getVerified() {
        return verified;
    }

    public void setVerified(byte verified) {
        this.verified = verified;
    }

    @Basic
    @Column(name = "student_id")
    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    @Basic
    @Column(name = "grade")
    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    @Basic
    @Column(name = "icpc_email")
    public String getIcpcEmail() {
        return icpcEmail;
    }

    public void setIcpcEmail(String icpcEmail) {
        this.icpcEmail = icpcEmail;
    }

    @Basic
    @Column(name = "status")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Basic
    @Column(name = "qq")
    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id &&
                isAdmin == user.isAdmin &&
                verified == user.verified &&
                Objects.equals(email, user.email) &&
                Objects.equals(phone, user.phone) &&
                Objects.equals(name, user.name) &&
                Objects.equals(password, user.password) &&
                Objects.equals(studentId, user.studentId) &&
                Objects.equals(grade, user.grade) &&
                Objects.equals(icpcEmail, user.icpcEmail) &&
                Objects.equals(status, user.status) &&
                Objects.equals(qq, user.qq);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, phone, name, password, isAdmin, verified, studentId, grade, icpcEmail, status, qq);
    }
}
