package cn.edu.scau.acm.acmer.model;

import java.util.List;

public class ContestTable {
    private String title;
    private List<String> problemList;
    private List<ContestRecordLine> contestRecordLines;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getProblemList() {
        return problemList;
    }

    public void setProblemList(List<String> problemList) {
        this.problemList = problemList;
    }

    public List<ContestRecordLine> getContestRecordLines() {
        return contestRecordLines;
    }

    public void setContestRecordLines(List<ContestRecordLine> contestRecordLines) {
        this.contestRecordLines = contestRecordLines;
    }
}
