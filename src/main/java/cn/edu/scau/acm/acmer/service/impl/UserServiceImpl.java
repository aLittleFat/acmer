package cn.edu.scau.acm.acmer.service.impl;

import cn.edu.scau.acm.acmer.entity.User;
import cn.edu.scau.acm.acmer.model.UserDto;
import cn.edu.scau.acm.acmer.service.AccountService;
import cn.edu.scau.acm.acmer.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private AccountService accountService;

    /**
     * 获取数据库中保存的用户信息，主要是加密后的密码
     * @return
     */
    public UserDto getUserInfo() {
        return getUserInfo();
    }

    /**
     * 获取数据库中保存的用户信息，主要是加密后的密码
     * @param userName
     * @return
     */
    public UserDto getUserInfo(String userName) {
        UserDto user = new UserDto();
        User u = accountService.getUserByEmail(userName);
        if(u == null) return null;
        user.setUserId(u.getId());
        user.setUsername(u.getEmail());
        user.setEncryptPwd(u.getPassword());
        return user;
    }

    /**
     * 获取用户角色列表，强烈建议从缓存中获取
     * @param userId
     * @return
     */
    public List<String> getUserRoles(int userId){
        List<String> roles = new ArrayList<>();
        if(accountService.isStudent(userId)){
            roles.add("student");
        }
        if(accountService.isAdmin(userId)){
            roles.add("admin");
        }
        return roles;
    }
}
