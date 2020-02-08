package cn.edu.scau.acm.acmer.service;

import cn.edu.scau.acm.acmer.entity.Student;
import cn.edu.scau.acm.acmer.entity.User;
import cn.edu.scau.acm.acmer.model.User_Student;
import org.springframework.data.domain.Page;

public interface AccountService {
    String registerUser(String email, String password, String phone, String name, String verifyCode);
    String registerStudent(String email, String password, String phone, String name, String verifyCode, int grade, String stuId);
    boolean isVerify(String email);
    void verifyAccount(int id);
    String verifyEmail(String email, String verifyCode);
    String verifyForgetPasswordEmail(String email, String verifyCode);
    String sendVerifyEmail(String email);
    String sendForgetPasswordVerifyEmail(String email);
    boolean isStudent(int id);
    boolean isAdmin(int id);
    String genVerifyCode();
    String forgetPassword(String email, String password, String verifyCode);
    Page<User_Student> getUserUnverify(Integer page, Integer size);
    void deleteAccount(Integer id);
    User_Student getUserStudentById(int id);
    String changePhoneAndIcpcEmail(String phone, String icpcEmail, int id);
}
