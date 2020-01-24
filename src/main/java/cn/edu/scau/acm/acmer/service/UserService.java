package cn.edu.scau.acm.acmer.service;

import cn.edu.scau.acm.acmer.model.UserDto;

import java.util.List;

public interface UserService {
//    public String generateJwtToken(String username);
//    public UserDto getJwtTokenInfo(String username);
//    public void deleteLoginInfo(String username);
    public UserDto getUserInfo(String userName);
    public List<String> getUserRoles(int userId);
}
