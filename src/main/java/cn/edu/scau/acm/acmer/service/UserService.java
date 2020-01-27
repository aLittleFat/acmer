package cn.edu.scau.acm.acmer.service;

import cn.edu.scau.acm.acmer.model.UserDto;

import java.util.List;

public interface UserService {
    UserDto getUserInfo(String userName);
    List<String> getUserRoles(int userId);
}
