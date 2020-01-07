package cn.edu.scau.acm.acmer.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class User {
    private int id;
    private String email;
    private String phone;
    private String name;
    private String password;
    private Byte isAdmin;
    private Byte verify;
    private Byte emailVerify;

    @Id
    @Column(name = "ID")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "Email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Basic
    @Column(name = "Phone")
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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
    @Column(name = "Password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Basic
    @Column(name = "IsAdmin")
    public Byte getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Byte isAdmin) {
        this.isAdmin = isAdmin;
    }

    @Basic
    @Column(name = "Verify")
    public Byte getVerify() {
        return verify;
    }

    public void setVerify(Byte verify) {
        this.verify = verify;
    }

    @Basic
    @Column(name = "EmailVerify")
    public Byte getEmailVerify() {
        return emailVerify;
    }

    public void setEmailVerify(Byte emailVerify) {
        this.emailVerify = emailVerify;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id &&
                Objects.equals(email, user.email) &&
                Objects.equals(phone, user.phone) &&
                Objects.equals(name, user.name) &&
                Objects.equals(password, user.password) &&
                Objects.equals(isAdmin, user.isAdmin) &&
                Objects.equals(verify, user.verify) &&
                Objects.equals(emailVerify, user.emailVerify);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, phone, name, password, isAdmin, verify, emailVerify);
    }
}
