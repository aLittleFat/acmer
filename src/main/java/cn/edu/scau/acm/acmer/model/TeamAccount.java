package cn.edu.scau.acm.acmer.model;

public class TeamAccount {
    private String title;
    private String handle;
    private String account;
    private String password;

    public TeamAccount(String title, String handle, String account, String password) {
        this.title = title;
        this.handle = handle;
        this.account = account;
        this.password = password;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHandle() {
        return handle;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
