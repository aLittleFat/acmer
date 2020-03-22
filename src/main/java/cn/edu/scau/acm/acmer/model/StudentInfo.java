package cn.edu.scau.acm.acmer.model;


import cn.edu.scau.acm.acmer.entity.Award;
import cn.edu.scau.acm.acmer.entity.Team;
import cn.edu.scau.acm.acmer.entity.User;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

public class StudentInfo {
    private User user;
    private Integer acNumber;
    private List<Award> awardList;
    private List<JSONObject> teams;
    private List<OjAcChart> ojAcCharts;
    private List<TagAcChart> tagAcCharts;


    public Integer getAcNumber() {
        return acNumber;
    }

    public void setAcNumber(Integer acNumber) {
        this.acNumber = acNumber;
    }

    public List<Award> getAwardList() {
        return awardList;
    }

    public void setAwardList(List<Award> awardList) {
        this.awardList = awardList;
    }

    public List<OjAcChart> getOjAcCharts() {
        return ojAcCharts;
    }

    public void setOjAcCharts(List<OjAcChart> ojAcCharts) {
        this.ojAcCharts = ojAcCharts;
    }

    public List<TagAcChart> getTagAcCharts() {
        return tagAcCharts;
    }

    public void setTagAcCharts(List<TagAcChart> tagAcCharts) {
        this.tagAcCharts = tagAcCharts;
    }

    public List<JSONObject> getTeams() {
        return teams;
    }

    public void setTeams(List<JSONObject> teams) {
        this.teams = teams;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
