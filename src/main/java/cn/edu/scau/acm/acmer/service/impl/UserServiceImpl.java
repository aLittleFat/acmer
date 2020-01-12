package cn.edu.scau.acm.acmer.service.impl;

import cn.edu.scau.acm.acmer.config.security.JwtUtils;
import cn.edu.scau.acm.acmer.entity.User;
import cn.edu.scau.acm.acmer.model.UserDto;
import cn.edu.scau.acm.acmer.repository.UserRepository;
import cn.edu.scau.acm.acmer.service.AccountService;
import cn.edu.scau.acm.acmer.service.UserService;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService {
    private static final String encryptSalt = "F12839WhsnnEV$#23b";

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private AccountService accountService;

    /**
     * 保存user登录信息，返回token
     * @param username
     */
    public String generateJwtToken(String username) {
        String salt = JwtUtils.generateSalt();
        stringRedisTemplate.opsForValue().set("token_salt:" + username, salt, 3600, TimeUnit.SECONDS);
        return JwtUtils.sign(username, salt, 3600); //生成jwt token，设置过期时间为1小时
    }

    /**
     * 获取上次token生成时的salt值和登录用户信息
     * @param username
     * @return
     */
    public UserDto getJwtTokenInfo(String username) {
        String salt = stringRedisTemplate.opsForValue().get("token_salt:" + username);
        UserDto user = getUserInfo(username);
        user.setSalt(salt);
        return user;
    }

    /**
     * 清除token信息
     * @param username 登录用户名
     */
    public void deleteLoginInfo(String username) {
        stringRedisTemplate.delete("token_salt:" + username);
    }

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
//        user.setUserId(1L);
//        user.setUsername("admin");
//        user.setEncryptPwd(new Sha256Hash("123456", encryptSalt).toHex());
        User u = accountService.getUserByEmail(userName);


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
