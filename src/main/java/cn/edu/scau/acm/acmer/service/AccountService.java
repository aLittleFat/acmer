package cn.edu.scau.acm.acmer.service;

import cn.edu.scau.acm.acmer.entity.User;
import org.springframework.data.domain.Page;

public interface AccountService {
    void register(String email, String password, String phone, String name, String verifyCode, Integer grade, String studentId, String qq) throws Exception;
    boolean isVerify(String email) throws Exception;
    void verifyAccount(int id) throws Exception;
    void verifyEmail(String email, String verifyCode) throws Exception;
    String verifyForgetPasswordEmail(String email, String verifyCode);
    void sendVerifyEmail(String email) throws Exception;
    void sendForgetPasswordVerifyEmail(String email) throws Exception;
    boolean isStudent(int id);
    boolean isAdmin(int id);
    String genVerifyCode();
    void forgetPassword(String email, String password, String verifyCode) throws Exception;
    Page<User> getUserUnVerified(Integer page, Integer size);
    void deleteAccount(Integer id);
    void changeInfo(int id, String phone, String icpcEmail, String qq);

    void retire(String studentId) throws Exception;

    Page<User> getRequestRetireUser(Integer page, Integer size);

    void changeUserStatus(Integer id, String status);

    void deleteStudents(String studentId, String myStudentId) throws Exception;

    void setAdmin(String studentId) throws Exception;
}
