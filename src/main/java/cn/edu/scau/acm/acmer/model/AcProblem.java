package cn.edu.scau.acm.acmer.model;


import cn.edu.scau.acm.acmer.entity.Problem;
import cn.edu.scau.acm.acmer.entity.ProblemAcRecord;

public class AcProblem {
    private Problem problem;
    private ProblemAcRecord problemAcRecord;

    public AcProblem(Problem problem, ProblemAcRecord problemAcRecord) {
        this.problem = problem;
        this.problemAcRecord = problemAcRecord;
    }

    public void setProblem(Problem problem) {
        this.problem = problem;
    }

    public void setProblemACRecord(ProblemAcRecord problemACRecord) {
        this.problemAcRecord = problemAcRecord;
    }

    public Problem getProblem() {
        return problem;
    }

    public ProblemAcRecord getProblemACRecord() {
        return problemAcRecord;
    }
}
