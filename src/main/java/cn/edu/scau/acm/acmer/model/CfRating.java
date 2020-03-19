package cn.edu.scau.acm.acmer.model;

public class CfRating {
    private String studentId;
    private Integer CfRating;
    private Integer solved;
    private Integer penalty;

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public Integer getCfRating() {
        return CfRating;
    }

    public void setCfRating(Integer cfRating) {
        CfRating = cfRating;
    }

    public Integer getSolved() {
        return solved;
    }

    public void setSolved(Integer solved) {
        this.solved = solved;
    }

    public Integer getPenalty() {
        return penalty;
    }

    public void setPenalty(Integer penalty) {
        this.penalty = penalty;
    }
}
