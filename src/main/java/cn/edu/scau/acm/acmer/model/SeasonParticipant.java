package cn.edu.scau.acm.acmer.model;

public class SeasonParticipant {
    private Integer teamId;
    private Integer seasonStudentId;
    private String participant;

    public SeasonParticipant(Integer teamId, Integer seasonStudentId, String participant) {
        if(teamId != 0)
            this.teamId = teamId;
        if(seasonStudentId != 0)
            this.seasonStudentId = seasonStudentId;
        this.participant = participant;
    }

    public Integer getTeamId() {
        return teamId;
    }

    public void setTeamId(Integer teamId) {
        this.teamId = teamId;
    }

    public Integer getSeasonStudentId() {
        return seasonStudentId;
    }

    public void setSeasonStudentId(Integer seasonStudentId) {
        this.seasonStudentId = seasonStudentId;
    }

    public String getParticipant() {
        return participant;
    }

    public void setParticipant(String participant) {
        this.participant = participant;
    }
}
