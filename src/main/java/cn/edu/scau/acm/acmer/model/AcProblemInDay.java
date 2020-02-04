package cn.edu.scau.acm.acmer.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AcProblemInDay {
    private String time;
    private List<AcProblem> acProblems;

    public AcProblemInDay() {
        acProblems = new ArrayList<>();
    }

    public String getTime() {
        return time;
    }

    public List<AcProblem> getAcProblems() {
        return acProblems;
    }

    public void setAcProblems(List<AcProblem> acProblems) {
        this.acProblems = acProblems;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
