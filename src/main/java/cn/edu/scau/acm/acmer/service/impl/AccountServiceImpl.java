package cn.edu.scau.acm.acmer.service.impl;

import cn.edu.scau.acm.acmer.entity.Student;
import cn.edu.scau.acm.acmer.entity.User;
import cn.edu.scau.acm.acmer.model.MyResponseEntity;
import cn.edu.scau.acm.acmer.model.User_Student;
import cn.edu.scau.acm.acmer.repository.StudentRepository;
import cn.edu.scau.acm.acmer.repository.UserRepository;
import cn.edu.scau.acm.acmer.service.AccountService;
import cn.edu.scau.acm.acmer.service.MailService;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class AccountServiceImpl implements AccountService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String encryptSalt = "F12839WhsnnEV$#23b";

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private MailService mailService;

    @Override
    public void registerUser(String email, String password, String phone, String name, String verifyCode) throws Exception {
        if(userRepository.findByEmail(email).isPresent()){
            throw new Exception("该邮箱已被注册");
        }

        String verifyStatus = verifyEmail(email, verifyCode);

        if(!verifyStatus.equals("true")){
            throw new Exception(verifyStatus);
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
    }

    @Override
    public void registerStudent(String email, String password, String phone, String name, String verifyCode, int grade, String stuId) throws Exception {
        if(userRepository.findByEmail(email).isPresent()){
            throw new Exception("该邮箱已被注册");
        }

        String verifyStatus = verifyEmail(email, verifyCode);

        if(!verifyStatus.equals("true")){
            throw new Exception(verifyStatus);
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
            throw new Exception("该学号已被注册");
        }
        userRepository.save(u);
        Student stu = new Student();
        stu.setId(stuId);
        stu.setGrade(grade);
        stu.setStatus("现役");
        stu.setUserId(userRepository.findByEmail(email).get().getId());
        studentRepository.save(stu);
    }

    @Override
    public boolean isVerify(String email) {
        return userRepository.findByEmail(email).get().getVerify() == (byte)1;
    }

    @Override
    public void verifyAccount(int id) throws Exception {
        try {
            User u = userRepository.findById(id).get();
            u.setVerify((byte) 1);
            userRepository.save(u);
            mailService.sendTextMail(u.getEmail(), "ACMER账号审核通过", "你在ACMER网站注册的账号 " + u.getEmail() + " 已通过审核，请尽快登录并完善个人信息");
        } catch (Exception e) {
            throw new Exception("服务器错误");
        }
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
    public String verifyForgetPasswordEmail(String email, String verifyCode) {
        String getCode = stringRedisTemplate.opsForValue().get(email + "_ForgetPasswordVerify");
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
    public void sendVerifyEmail(String email) throws Exception {
        if(userRepository.findByEmail(email).isPresent()){
            throw new Exception("该邮箱已被注册");
        }
        String verifyCode = stringRedisTemplate.opsForValue().get(email + "_Verify");
        if(verifyCode == null){
            verifyCode = genVerifyCode();
            stringRedisTemplate.opsForValue().set(email + "_Verify", verifyCode, 10, TimeUnit.MINUTES);
        }
        mailService.sendTextMail(email, "SCAUACMER网站邮箱验证码", "验证码为：" + verifyCode + ". 有效期为10分钟.");
    }

    @Override
    public void sendForgetPasswordVerifyEmail(String email) throws Exception {
        if(!userRepository.findByEmail(email).isPresent()){
            throw new Exception("该邮箱不存在");
        }
        String verifyCode = stringRedisTemplate.opsForValue().get(email + "_ForgetPasswordVerify");
        if(verifyCode == null){
            verifyCode = genVerifyCode();
            stringRedisTemplate.opsForValue().set(email + "_ForgetPasswordVerify", verifyCode, 10, TimeUnit.MINUTES);
        }
        mailService.sendTextMail(email, "SCAUACMER网站邮箱验证码", "验证码为：" + verifyCode + ". 有效期为10分钟.");
    }

    @Override
    public boolean isStudent(int id) {
        return studentRepository.findByUserId(id).isPresent();
    }

    @Override
    public boolean isAdmin(int id) {
        return (userRepository.findById(id).get().getIsAdmin() == (byte)1);
    }

    @Override
    public String genVerifyCode() {
        StringBuffer code = new StringBuffer();
        Random rand = new Random();
        for(int i = 0; i < 6; ++i){
            code.append(rand.nextInt(10));
        }
        logger.info(code.toString());
        return code.toString();
    }

    @Override
    public void forgetPassword(String email, String password, String verifyCode) throws Exception {
        Optional<User> u = userRepository.findByEmail(email);
        if(!u.isPresent()){
            throw new Exception("该邮箱不存在");
        }
        String verifyStatus = verifyForgetPasswordEmail(email, verifyCode);
        if(verifyStatus != "true"){
            throw new Exception(verifyStatus);
        }
        User user = u.get();
        user.setPassword(new Sha256Hash(password, encryptSalt).toHex());
        userRepository.save(user);
    }

    @Override
    public Page<User_Student> getUserUnverify(Integer page, Integer size) {
        Pageable pr = PageRequest.of(page - 1, size, Sort.Direction.ASC, "name");
        return userRepository.findAllUnVerify(pr);
    }

    @Override
    public void deleteAccount(Integer id) {
        User u = userRepository.findById(id).get();
        Optional<Student> student = studentRepository.findByUserId(id);
        student.ifPresent(value -> studentRepository.delete(value));
        userRepository.delete(u);
        mailService.sendTextMail(u.getEmail(), "ACMER账号审核不通过", "你在ACMER网站注册的账号 " + u.getEmail() + " 没有通过审核，账号已删除，请重新注册");
    }

    @Override
    public User_Student getUserStudentById(int id) {
        return new User_Student(userRepository.findById(id).get(), studentRepository.findByUserId(id).orElse(null));
    }

    @Override
    public void changePhoneAndIcpcEmail(String phone, String icpcEmail, int id) {
        User u = userRepository.findById(id).get();
        Student stu = studentRepository.findByUserId(id).get();
//        if (!phone.equals("")) {
            u.setPhone(phone);
            userRepository.save(u);
//        }
//        if (!icpcEmail.equals("")) {
            stu.setIcpcEmail(icpcEmail);
            studentRepository.save(stu);
//        }
    }

}
