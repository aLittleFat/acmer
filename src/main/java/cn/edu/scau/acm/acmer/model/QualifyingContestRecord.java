package cn.edu.scau.acm.acmer.model;

public class QualifyingContestRecord {
    private Integer teamId;
    private Integer seasonStudentId;
    private Long solved;
    private Integer penalty;
    private Double score;

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

    public Long getSolved() {
        return solved;
    }

    public void setSolved(Long solved) {
        this.solved = solved;
    }

    public Integer getPenalty() {
        return penalty;
    }

    public void setPenalty(Integer penalty) {
        this.penalty = penalty;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }
}
