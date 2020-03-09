package cn.edu.scau.acm.acmer.model;

public class MyTeamMenu {
    private Integer id;
    private String teamName;
    private String seasonName;

    public MyTeamMenu(Integer id, String teamName, String seasonName) {
        this.id = id;
        this.teamName = teamName;
        this.seasonName = seasonName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getSeasonName() {
        return seasonName;
    }

    public void setSeasonName(String seasonName) {
        this.seasonName = seasonName;
    }
}
