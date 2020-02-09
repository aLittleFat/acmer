package cn.edu.scau.acm.acmer.model;

public class MyResponseEntity<T> {
    private int status;
    private String msg;
    private T data;

    public MyResponseEntity(int status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public MyResponseEntity(int status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public MyResponseEntity(int status, T data) {
        this.status = status;
        this.data = data;
    }

    public MyResponseEntity(String msg) {
        this.status = 1;
        this.msg = msg;
    }

    public MyResponseEntity(T data) {
        this.status = 0;
        this.data = data;
    }

    public MyResponseEntity() {
        this.status = 0;
    }

    public int getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
