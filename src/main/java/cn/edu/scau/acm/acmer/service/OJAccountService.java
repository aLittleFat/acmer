package cn.edu.scau.acm.acmer.service;

public interface OJAccountService {

    boolean checkOjAccount(String ojName, String username, String password);

    String addOjAccount(String ojName, String username, String password, int id);

    String getOjAccount(String ojName, int userId);

    String deleteOjAccount(String ojName, int userId);

    String changeOjAccount(String ojName, String username, String password, int userId);
}
