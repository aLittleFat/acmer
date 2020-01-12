package cn.edu.scau.acm.acmer.service;

import cn.edu.scau.acm.acmer.entity.User;

public interface AccountService {
    public String registerUser(String email, String password, String phone, String name, String verifyCode);
    public User getUserByEmail(String email);
    public String registerStudent(String email, String password, String phone, String name, String verifyCode, int grade, String stuId);
    public boolean isVerify(String email);
    public void login();
    public void verifyAccount();
    public String verifyEmail(String email, String verifyCode);
    public void sendVerifyEmail(String email);
    public boolean isStudent(int id);
    public boolean isAdmin(int id);
    public String genEmailVerifyCode();
}
