package cn.edu.scau.acm.acmer.service;

import cn.edu.scau.acm.acmer.model.MyResponseEntity;

public interface OJAccountService {

    boolean checkOjAccount(String ojName, String username, String password);

    MyResponseEntity<Void> addOjAccount(String ojName, String username, String password, int id);

    MyResponseEntity<String> getOjAccount(String ojName, int userId);

    MyResponseEntity<Void> deleteOjAccount(String ojName, int userId);

    MyResponseEntity<Void> changeOjAccount(String ojName, String username, String password, int userId);
}
