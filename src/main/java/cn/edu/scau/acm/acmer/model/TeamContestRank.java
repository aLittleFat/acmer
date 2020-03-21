package cn.edu.scau.acm.acmer.model;

import cn.edu.scau.acm.acmer.entity.Award;

import java.util.List;

public class TeamContestRank {
    public TeamContestRank(Integer teamId, String nameCn, String nameEn, String students, Long contestNumber) {
        this.teamId = teamId;
        this.nameCn = nameCn;
        this.nameEn = nameEn;
        this.students = students;
        this.contestNumber = contestNumber;
    }

    private Integer teamId;
    private String nameCn;
    private String nameEn;
    private String students;
    private Long contestNumber;
    private List<Award> awardList;

    public Integer getTeamId() {
        return teamId;
    }

    public void setTeamId(Integer teamId) {
        this.teamId = teamId;
    }

    public String getNameCn() {
        return nameCn;
    }

    public void setNameCn(String nameCn) {
        this.nameCn = nameCn;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getStudents() {
        return students;
    }

    public void setStudents(String students) {
        this.students = students;
    }

    public Long getContestNumber() {
        return contestNumber;
    }

    public void setContestNumber(Long contestNumber) {
        this.contestNumber = contestNumber;
    }

    public List<Award> getAwardList() {
        return awardList;
    }

    public void setAwardList(List<Award> awardList) {
        this.awardList = awardList;
    }
}
