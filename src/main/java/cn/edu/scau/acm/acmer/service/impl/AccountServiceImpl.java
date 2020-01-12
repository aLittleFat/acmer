package cn.edu.scau.acm.acmer.service.impl;

import cn.edu.scau.acm.acmer.entity.Student;
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
import java.util.Random;
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
    public String registerUser(String email, String password, String phone, String name, String verifyCode) {
        if(userRepository.findByEmail(email) != null){
            return "该邮箱已被注册";
        }

        String verifyStatus = verifyEmail(email, verifyCode);

        if(!verifyStatus.equals("true")){
            return verifyStatus;
        }
        User u = new User();
        u.setEmail(email);
        String enPassword = new Sha256Hash(password, encryptSalt).toHex();
        u.setPassword(enPassword);
        u.setPhone(phone);
        u.setName(name);
        u.setIsAdmin((byte) 1);
        if(userRepository.count() == 0){
            u.setVerify((byte) 1);
        }
        else {
            u.setVerify((byte) 0);
        }
        userRepository.save(u);
        return "true";
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public String registerStudent(String email, String password, String phone, String name, String verifyCode, int grade, String stuId) {
        if(userRepository.findByEmail(email) != null){
            return "该邮箱已被注册";
        }

        String verifyStatus = verifyEmail(email, verifyCode);

        if(!verifyStatus.equals("true")){
            return verifyStatus;
        }
        User u = new User();
        u.setEmail(email);
        String enPassword = new Sha256Hash(password, encryptSalt).toHex();
        u.setPassword(enPassword);
        u.setPhone(phone);
        u.setName(name);
        if(userRepository.count() == 0){
            u.setIsAdmin((byte) 1);
        }
        else {
            u.setIsAdmin((byte) 0);
        }
        if(userRepository.count() == 0){
            u.setVerify((byte) 1);
        }
        else {
            u.setVerify((byte) 0);
        }
        if(studentRepository.findById(stuId).isPresent()){
            return "该学号已被注册";
        }
        userRepository.save(u);
        Student stu = new Student();
        stu.setId(stuId);
        stu.setGrade(grade);
        stu.setStatus("现役");
        stu.setUserId(userRepository.findByEmail(email).getId());
        studentRepository.save(stu);
        return "true";
    }

    @Override
    public boolean isVerify(String email) {
        return userRepository.findByEmail(email).getVerify() == (byte)1;
    }


    @Override
    public void login() {

    }

    @Override
    public void verifyAccount() {

    }

    @Override
    public String verifyEmail(String email, String verifyCode) {
        String getCode = stringRedisTemplate.opsForValue().get(email + "_Verify");
        if(getCode == null){
            return "验证码已过期";
        }
        if(getCode.equals(verifyCode)){
            return "true";
        }
        else{
            return "验证码错误";
        }
    }

    @Override
    public void sendVerifyEmail(String email) {
        String verifyCode = stringRedisTemplate.opsForValue().get(email + "_Verify");
        if(verifyCode == null){
            verifyCode = genEmailVerifyCode();
            stringRedisTemplate.opsForValue().set(email + "_Verify", verifyCode, 10, TimeUnit.MINUTES);
        }
        mailService.sendTextMail(email, "SCAUACMER网站邮箱验证码", "验证码为：" + verifyCode + ". 有效期为10分钟.");
    }

    @Override
    public boolean isStudent(int id) {
        return studentRepository.findByUserId(id) != null;
    }

    @Override
    public boolean isAdmin(int id) {
        return (userRepository.findById(id).getIsAdmin() == (byte)1);
    }

    @Override
    public String genEmailVerifyCode() {
        StringBuffer code = new StringBuffer();
        Random rand = new Random();
        for(int i = 0; i < 6; ++i){
            code.append(rand.nextInt(10));
        }
        logger.info(code.toString());
        return code.toString();
    }

}
