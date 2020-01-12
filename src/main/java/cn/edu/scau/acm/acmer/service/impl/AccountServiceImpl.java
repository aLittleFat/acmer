package cn.edu.scau.acm.acmer.service.impl;

import cn.edu.scau.acm.acmer.entity.User;
import cn.edu.scau.acm.acmer.repository.StudentRepository;
import cn.edu.scau.acm.acmer.repository.UserRepository;
import cn.edu.scau.acm.acmer.service.AccountService;
import cn.edu.scau.acm.acmer.service.MailService;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Service
public class AccountServiceImpl implements AccountService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String encryptSalt = "F12839WhsnnEV$#23b";

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    MailService mailService;

    @Override
    public User registerUser(String email, String password, String phone, String name) {
        User u = new User();
        u.setEmail(email);
        String enPassword = new Sha256Hash(password, encryptSalt).toHex();
        logger.info(enPassword);
        u.setPassword(enPassword);
        u.setPhone(phone);
        u.setName(name);
        u.setIsAdmin((byte) 1);
        u.setEmailVerify((byte) 0);
        if(userRepository.count() == 0){
            u.setVerify((byte) 1);
        }
        else {
            u.setVerify((byte) 0);
        }
        return userRepository.save(u);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void registerStudent(String email, String password, String phone, String name, int grade, String id, String vjId, String vjPassword) {

    }

    @Override
    public void login() {

    }

    @Override
    public void verifyAccount() {

    }

    @Override
    public String verifyEmail(String email, String verifyCode) {
        User u = getUserByEmail(email);
        if(u == null){
            return "邮箱不存在";
        }
        if(u.getEmailVerify() == (byte)1){
            return "邮箱已验证，无需再验证";
        }
        String getCode = stringRedisTemplate.opsForValue().get(email + "_Verify");
        if(getCode == null){
            sendVerifyEmail(email);
            return "验证码已过期，已重发验证邮件";
        }
        if(u != null && getCode.equals(verifyCode)){
            u.setEmailVerify((byte)1);
            userRepository.save(u);
            return "验证成功";
        }
        else{
            return "验证失败";
        }
    }

    @Override
    public boolean isEmailVerify(String email) {
        boolean isVerify = getUserByEmail(email).getEmailVerify() == (byte)1;
        if(!isVerify) {
            String verifyCode = stringRedisTemplate.opsForValue().get(email + "_Verify");
            if(verifyCode == null) {
                sendVerifyEmail(email);
            }
        }
        return isVerify;
    }

    @Override
    public void sendVerifyEmail(String email) {
        String verifyCode = stringRedisTemplate.opsForValue().get(email + "_Verify");
        if(verifyCode == null){
            verifyCode = new Sha256Hash(email + LocalDateTime.now().toString(), encryptSalt).toHex();
            stringRedisTemplate.opsForValue().set(email + "_Verify", verifyCode, 1800, TimeUnit.SECONDS);
        }
        String url = "http://localhost:8080/auth/verifyEmail?email=" + email + "&verifyCode=" + verifyCode;
        mailService.sendTextMail(email, "ACMER网站邮箱验证", "请通过链接完成验证，有效期30min：" + url);
    }

    @Override
    public boolean isStudent(int id) {
        return studentRepository.findByUserId(id) != null;
    }

    @Override
    public boolean isAdmin(int id) {
        return (userRepository.findById(id).getIsAdmin() == (byte)1);
    }

}
