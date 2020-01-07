package cn.edu.scau.acm.acmer.controller.api;

import cn.edu.scau.acm.acmer.entity.User;
import cn.edu.scau.acm.acmer.repository.UserRepository;
import cn.edu.scau.acm.acmer.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    AccountService accountService;

    @PostMapping("/register")
    public String registerUser(@RequestBody Map<String,String> registerUser){
        System.out.println(registerUser.get("email") + " " + registerUser.get("password") + " " + registerUser.get("phone") + " " + registerUser.get("name"));
        return accountService.registerUser(registerUser.get("email"), registerUser.get("password"), registerUser.get("phone"), registerUser.get("name")).toString();
    }
}