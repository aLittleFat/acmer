package cn.edu.scau.acm.acmer.model;

import cn.edu.scau.acm.acmer.entity.Student;
import cn.edu.scau.acm.acmer.entity.User;

public class User_Student {

    public User_Student(User user, Student student) {
        this.user = user;
        this.student = student;
    }

    private User user;

    private Student student;

    public Student getStudent() {
        return student;
    }

    public User getUser() {
        return user;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "User_Student{" +
                "user=" + user +
                ", student=" + student +
                '}';
    }
}
