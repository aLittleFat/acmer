package cn.edu.scau.acm.acmer.service.impl;

import cn.edu.scau.acm.acmer.entity.User;
import cn.edu.scau.acm.acmer.repository.StudentRepository;
import cn.edu.scau.acm.acmer.repository.UserRepository;
import cn.edu.scau.acm.acmer.service.AccountService;
import cn.edu.scau.acm.acmer.service.MailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class AccountServiceImpl implements AccountService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

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
        String enPassword = (new BCryptPasswordEncoder()).encode(password);
        System.out.println(enPassword);
        u.setPassword(enPassword);
        u.setPhone(phone);
        u.setName(name);
        u.setIsAdmin((byte) 1);
        u.setEmailVerify((byte) 0);
        u.setVerify((byte) 0);
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
        String getCode = stringRedisTemplate.opsForValue().get(email + "_Verify");
        logger.info("开始校验邮箱");
        logger.info(email);
        logger.info(verifyCode);
        logger.info(getCode);
        if(u != null && getCode != null && getCode.equals(verifyCode)){
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
        boolean isVerify = getUserByEmail(email).getVerify() == (byte)1;
//        System.out.println(stringRedisTemplate.opsForValue().get(email + "_Verify"));
//        if(stringRedisTemplate.opsForValue().get(email + "_Verify") == null) {
            sendVerifyEmail(email);
//        }
        return isVerify;
    }

    @Override
    public void sendVerifyEmail(String email) {
        String verifyCode = stringRedisTemplate.opsForValue().get(email + "_Verify");
        if(verifyCode == null){
            verifyCode = (new BCryptPasswordEncoder()).encode(email);
            stringRedisTemplate.opsForValue().set(email + "_Verify", verifyCode, 1800, TimeUnit.SECONDS);
        }
        String url = "http://localhost:8080/auth/verifyEmail?email=" + email + "&verifyCode=" + verifyCode;
        mailService.sendTextMail(email, "ACMER网站邮箱验证", "请通过链接完成验证，有效期30min：" + url);
    }

}
