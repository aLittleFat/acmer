package cn.edu.scau.acm.acmer.model;


import cn.edu.scau.acm.acmer.entity.Award;
import cn.edu.scau.acm.acmer.entity.Team;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

public class StudentInfo {
    private String name;
    private Integer grade;
    private Integer acNumber;
    private Integer cfRating;
    private List<Award> awardList;
    private List<JSONObject> teams;
    private List<OjAcChart> ojAcCharts;
    private List<TagAcChart> tagAcCharts;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getGrade() {
        return grade;
    }

    public Integer getAcNumber() {
        return acNumber;
    }

    public void setAcNumber(Integer acNumber) {
        this.acNumber = acNumber;
    }

    public Integer getCfRating() {
        return cfRating;
    }

    public void setCfRating(Integer cfRating) {
        this.cfRating = cfRating;
    }

    public List<Award> getAwardList() {
        return awardList;
    }

    public void setAwardList(List<Award> awardList) {
        this.awardList = awardList;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
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
}
