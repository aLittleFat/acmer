package cn.edu.scau.acm.acmer.model;

import cn.edu.scau.acm.acmer.entity.Team;
import cn.edu.scau.acm.acmer.entity.User;

import java.util.List;

public class TeamWithUsers {

    public TeamWithUsers(Team team, List<User> users) {
        this.team = team;
        this.users = users;
    }

    private Team team;
    private List<User> users;

    public List<User> getUsers() {
        return users;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
