package cn.edu.scau.acm.acmer.service.impl;

import cn.edu.scau.acm.acmer.entity.User;
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
    private UserRepository userRepository;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private MailService mailService;

    @Override
    public void register(String email, String password, String phone, String name, String verifyCode, Integer grade, String studentId, String qq) throws Exception {
        if(userRepository.findByEmail(email).isPresent()){
            throw new Exception("该邮箱已被注册");
        }
        if(studentId != null && userRepository.findByStudentId(studentId).isPresent()) {
            throw new Exception("该学号已被注册");
        }

        verifyEmail(email, verifyCode);

        User u = new User();
        u.setEmail(email);
        String enPassword = new Sha256Hash(password, encryptSalt).toHex();
        u.setPassword(enPassword);
        u.setPhone(phone);
        u.setName(name);
        u.setQq(qq);
        if(userRepository.count() == 0){
            u.setIsAdmin((byte) 1);
        }
        else {
            u.setIsAdmin((byte) 0);
        }
        if(userRepository.count() == 0){
            u.setVerified((byte) 1);
        }
        else {
            u.setVerified((byte) 0);
        }

        if(studentId != null) {
            u.setStudentId(studentId);
            u.setGrade(grade);
            u.setStatus("现役");
        }
        userRepository.save(u);
    }

    @Override
    public boolean isVerify(String email) {
        return userRepository.findByEmail(email).get().getVerified() == (byte)1;
    }

    @Override
    public void verifyAccount(int id) throws Exception {
        try {
            User u = userRepository.findById(id).get();
            u.setVerified((byte) 1);
            userRepository.save(u);
            mailService.sendTextMail(u.getEmail(), "ACMER账号审核通过", "你在ACMER网站注册的账号 " + u.getEmail() + " 已通过审核，请尽快登录并完善个人信息");
        } catch (Exception e) {
            throw new Exception("服务器错误");
        }
    }

    @Override
    public void verifyEmail(String email, String verifyCode) throws Exception {
        String getCode = stringRedisTemplate.opsForValue().get(email + "_Verify");
        if(getCode == null){
            throw new Exception("验证码已过期");
        }
        if(!getCode.equals(verifyCode)){
            throw new Exception("验证码错误");
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
        return userRepository.findById(id).get().getStudentId() != null;
    }

    @Override
    public boolean isAdmin(int id) {
        return (userRepository.findById(id).get().getIsAdmin() == (byte)1);
    }

    @Override
    public String genVerifyCode() {
        StringBuilder code = new StringBuilder();
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
        if(u.isEmpty()){
            throw new Exception("该邮箱不存在");
        }
        String verifyStatus = verifyForgetPasswordEmail(email, verifyCode);
        if(!verifyStatus.equals("true")){
            throw new Exception(verifyStatus);
        }
        User user = u.get();
        user.setPassword(new Sha256Hash(password, encryptSalt).toHex());
        userRepository.save(user);
    }

    @Override
    public Page<User> getUserUnVerified(Integer page, Integer size) {
        Pageable pr = PageRequest.of(page - 1, size, Sort.Direction.ASC, "name");
        return userRepository.findAllByVerifiedEquals(pr, (byte)0);
    }

    @Override
    public void deleteAccount(Integer id) {
        User u = userRepository.findById(id).get();
        userRepository.delete(u);
        mailService.sendTextMail(u.getEmail(), "ACMER账号审核不通过", "你在ACMER网站注册的账号 " + u.getEmail() + " 没有通过审核，账号已删除，请重新注册");
    }

    @Override
    public void changeInfo(int id, String phone, String icpcEmail, String qq) {
        User u = userRepository.findById(id).get();
        u.setPhone(phone);
        u.setIcpcEmail(icpcEmail);
        u.setQq(qq);
        userRepository.save(u);
    }

    @Override
    public void retire(String studentId) throws Exception {
        User u = userRepository.findByStudentId(studentId).get();
        if(!u.getStatus().equals("现役")) {
            throw new Exception("不是现役，不能申请退役");
        }
        u.setStatus("申请退役");
        userRepository.save(u);
    }

    @Override
    public Page<User> getRequestRetireUser(Integer page, Integer size) {
        Pageable pr = PageRequest.of(page - 1, size, Sort.Direction.ASC, "name");
        return userRepository.findAllByStatus(pr, "申请退役");
    }

    @Override
    public void changeUserStatus(Integer id, String status) {
        User u = userRepository.findById(id).get();
        u.setStatus(status);
        userRepository.save(u);
    }

}
