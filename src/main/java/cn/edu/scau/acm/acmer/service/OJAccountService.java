package cn.edu.scau.acm.acmer.service;

import cn.edu.scau.acm.acmer.model.MyResponseEntity;

public interface OJAccountService {

    boolean checkOjAccount(String ojName, String username, String password);

    void addOjAccount(String ojName, String username, String password, int id) throws Exception;

    String getOjAccount(String ojName, int userId);

    void deleteOjAccount(String ojName, int userId) throws Exception;

    void changeOjAccount(String ojName, String username, String password, int userId) throws Exception;
}
