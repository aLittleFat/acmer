package cn.edu.scau.acm.acmer.service;

import cn.edu.scau.acm.acmer.entity.Student;
import cn.edu.scau.acm.acmer.entity.User;
import cn.edu.scau.acm.acmer.model.MyResponseEntity;
import cn.edu.scau.acm.acmer.model.User_Student;
import org.springframework.data.domain.Page;

public interface AccountService {
    MyResponseEntity<Void> registerUser(String email, String password, String phone, String name, String verifyCode);
    MyResponseEntity<Void> registerStudent(String email, String password, String phone, String name, String verifyCode, int grade, String stuId);
    boolean isVerify(String email);
    MyResponseEntity<Void> verifyAccount(int id);
    String verifyEmail(String email, String verifyCode);
    String verifyForgetPasswordEmail(String email, String verifyCode);
    MyResponseEntity<Void> sendVerifyEmail(String email);
    MyResponseEntity<Void> sendForgetPasswordVerifyEmail(String email);
    boolean isStudent(int id);
    boolean isAdmin(int id);
    String genVerifyCode();
    MyResponseEntity<Void> forgetPassword(String email, String password, String verifyCode);
    MyResponseEntity<Page<User_Student>> getUserUnverify(Integer page, Integer size);
    MyResponseEntity<Void> deleteAccount(Integer id);
    MyResponseEntity<User_Student> getUserStudentById(int id);
    MyResponseEntity<Void> changePhoneAndIcpcEmail(String phone, String icpcEmail, int id);
}
