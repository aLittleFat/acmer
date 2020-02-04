package cn.edu.scau.acm.acmer.model;


import cn.edu.scau.acm.acmer.entity.Problem;
import cn.edu.scau.acm.acmer.entity.ProblemACRecord;

public class AcProblem {
    private Problem problem;
    private ProblemACRecord problemACRecord;

    public AcProblem(Problem problem, ProblemACRecord problemACRecord) {
        this.problem = problem;
        this.problemACRecord = problemACRecord;
    }

    public void setProblem(Problem problem) {
        this.problem = problem;
    }

    public void setProblemACRecord(ProblemACRecord problemACRecord) {
        this.problemACRecord = problemACRecord;
    }

    public Problem getProblem() {
        return problem;
    }

    public ProblemACRecord getProblemACRecord() {
        return problemACRecord;
    }
}
