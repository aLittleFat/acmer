package cn.edu.scau.acm.acmer.service;

public interface OJAccountService {
    String addVjAccount(String username, String password, int id);

    boolean checkVjLoginStatus();
    void vjLogout();
}
