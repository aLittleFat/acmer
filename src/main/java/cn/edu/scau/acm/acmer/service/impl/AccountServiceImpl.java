package cn.edu.scau.acm.acmer.service.impl;

import cn.edu.scau.acm.acmer.entity.User;
import cn.edu.scau.acm.acmer.repository.StudentRepository;
import cn.edu.scau.acm.acmer.repository.UserRepository;
import cn.edu.scau.acm.acmer.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    UserRepository userRepository;

    @Override
    public User registerUser(String email, String password, String phone, String name) {
        User u = new User();
        u.setEmail(email);
        String enPassword = (new BCryptPasswordEncoder()).encode(password);
        System.out.println(enPassword);
        u.setPassword(enPassword);
        u.setPhone(phone);
        u.setName(name);
        u.setIsAdmin((byte) 1);
        u.setEmailVerify((byte) 0);
        u.setVerify((byte) 0);
        return userRepository.save(u);
    }

    @Override
    public void registerStudent(String email, String password, String phone, String name, int grade, String id, String vjId, String vjPassword) {

    }

    @Override
    public void login() {

    }

    @Override
    public void verifyAccount() {

    }

    @Override
    public void verifyEmail() {

    }

}
