package cn.edu.scau.acm.acmer.model;

public class QualifyingAcChart implements Comparable<QualifyingAcChart> {
    private String name;
    private Long 赛时解题数;
    private Long 补题数;

    public QualifyingAcChart(String name, Long 赛时解题数, Long 补题数) {
        this.name = name;
        this.赛时解题数 = 赛时解题数;
        this.补题数 = 补题数;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long get赛时解题数() {
        return 赛时解题数;
    }

    public void set赛时解题数(Long 赛时解题数) {
        this.赛时解题数 = 赛时解题数;
    }

    public Long get补题数() {
        return 补题数;
    }

    public void set补题数(Long 补题数) {
        this.补题数 = 补题数;
    }

    @Override
    public int compareTo(QualifyingAcChart qualifyingAcChart) {
        Long sum1 = 赛时解题数 + 补题数;
        Long sum2 = qualifyingAcChart.get赛时解题数() + qualifyingAcChart.get补题数();
        if (sum1.equals(sum2)) {
            return -补题数.compareTo(qualifyingAcChart.get补题数());
        } else {
            return -sum1.compareTo(sum2);
        }
    }
}
