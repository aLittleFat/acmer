package cn.edu.scau.acm.acmer.service;

import cn.edu.scau.acm.acmer.entity.User;
import cn.edu.scau.acm.acmer.model.User_Student;
import org.springframework.data.domain.Page;

public interface AccountService {
    public String registerUser(String email, String password, String phone, String name, String verifyCode);
    public User getUserByEmail(String email);
    public String registerStudent(String email, String password, String phone, String name, String verifyCode, int grade, String stuId);
    public boolean isVerify(String email);
    public void verifyAccount(int id);
    public String verifyEmail(String email, String verifyCode);
    public String verifyForgetPasswordEmail(String email, String verifyCode);
    public String sendVerifyEmail(String email);
    public String sendForgetPasswordVerifyEmail(String email);
    public boolean isStudent(int id);
    public boolean isAdmin(int id);
    public String genEmailVerifyCode();
    public String forgetPassword(String email, String password, String verifyCode);

    public User getUserById(int id);

    public Page<User_Student> getUserUnverify(Integer page, Integer size);

    public void deleteAccount(Integer id);
}
