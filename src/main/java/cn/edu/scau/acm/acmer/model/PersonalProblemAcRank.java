package cn.edu.scau.acm.acmer.model;

import cn.edu.scau.acm.acmer.entity.Student;
import cn.edu.scau.acm.acmer.entity.TeamAward;
import cn.edu.scau.acm.acmer.entity.User;

import java.util.ArrayList;
import java.util.List;

public class PersonalProblemAcRank implements Comparable<PersonalProblemAcRank> {
    private User user;
    private Student student;
    private int acNum;
    private List<TeamAward> teamAwards;

    public PersonalProblemAcRank() {
        teamAwards = new ArrayList<>();
    }

    public Student getStudent() {
        return student;
    }

    public int getAcNum() {
        return acNum;
    }

    public List<TeamAward> getTeamAwards() {
        return teamAwards;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public void setAcNum(int acNum) {
        this.acNum = acNum;
    }

    public void setTeamAwards(List<TeamAward> teamAwards) {
        this.teamAwards = teamAwards;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public int compareTo(PersonalProblemAcRank personalProblemAcRank) {
        if(this.getAcNum() < personalProblemAcRank.getAcNum()) {
            return 1;
        }
        else return -1;
    }
}
