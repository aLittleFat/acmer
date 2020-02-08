package cn.edu.scau.acm.acmer.service.impl;

import cn.edu.scau.acm.acmer.entity.User;
import cn.edu.scau.acm.acmer.model.UserDto;
import cn.edu.scau.acm.acmer.repository.UserRepository;
import cn.edu.scau.acm.acmer.service.AccountService;
import cn.edu.scau.acm.acmer.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

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
        Optional<User> u = userRepository.findByEmail(userName);
        if(!u.isPresent()) return null;
        user.setUserId(u.get().getId());
        user.setUsername(u.get().getEmail());
        user.setEncryptPwd(u.get().getPassword());
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
