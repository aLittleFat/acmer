package cn.edu.scau.acm.acmer.service;

import cn.edu.scau.acm.acmer.model.User_Student;
import org.springframework.data.domain.Page;

public interface AccountService {
    void registerUser(String email, String password, String phone, String name, String verifyCode) throws Exception;
    void registerStudent(String email, String password, String phone, String name, String verifyCode, int grade, String stuId) throws Exception;
    boolean isVerify(String email);
    void verifyAccount(int id) throws Exception;
    String verifyEmail(String email, String verifyCode);
    String verifyForgetPasswordEmail(String email, String verifyCode);
    void sendVerifyEmail(String email) throws Exception;
    void sendForgetPasswordVerifyEmail(String email) throws Exception;
    boolean isStudent(int id);
    boolean isAdmin(int id);
    String genVerifyCode();
    void forgetPassword(String email, String password, String verifyCode) throws Exception;
    Page<User_Student> getUserUnverify(Integer page, Integer size);
    void deleteAccount(Integer id);
    User_Student getUserStudentById(int id);
    void changePhoneAndIcpcEmail(String phone, String icpcEmail, int id);
}
