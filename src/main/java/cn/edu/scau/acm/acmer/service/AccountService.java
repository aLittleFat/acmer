package cn.edu.scau.acm.acmer.service;

import cn.edu.scau.acm.acmer.entity.User;

public interface AccountService {
    public User registerUser(String email, String password, String phone, String Name);
    public User getUserByEmail(String email);
    public void registerStudent(String email, String password, String phone, String Name,int grade, String id, String vjId, String vjPassword);
    public void login();
    public void verifyAccount();
    public String verifyEmail(String email, String verifyCode);
    public boolean isEmailVerify(String email);
    public void sendVerifyEmail(String email);
    public boolean isStudent(int id);
    public boolean isAdmin(int id);
}
