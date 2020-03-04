package cn.edu.scau.acm.acmer.model;

public class TagAcChart {
    private String tagName;
    private Long number;

    public TagAcChart(String ojName, Long number) {
        this.tagName = ojName;
        this.number = number;
    }

    public Long getNumber() {
        return number;
    }

    public String getTagName() {
        return tagName;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }
}
