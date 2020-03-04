package cn.edu.scau.acm.acmer.model;

public class OjAcChart {
    private String ojName;
    private Long number;

    public OjAcChart(String ojName, Long number) {
        this.ojName = ojName;
        this.number = number;
    }

    public Long getNumber() {
        return number;
    }

    public String getOjName() {
        return ojName;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public void setOjName(String ojName) {
        this.ojName = ojName;
    }
}
