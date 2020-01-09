package cn.edu.scau.acm.acmer.controller.api;

import cn.edu.scau.acm.acmer.service.AccountService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    AccountService accountService;

    @ApiOperation(value = "教师注册")
    @PostMapping("/register/teacher")
    public String registerTeacher(String email, String password, String phone, String name){
        System.out.println(email + " " + password + " " + phone + " " + name);
        return accountService.registerUser(email, password, phone, name).toString();
    }

    @ApiOperation(value = "学生注册")
    @PostMapping("/register/student")
    public String registerStudent(@RequestBody Map<String,String> registerUser){
        System.out.println(registerUser.get("email") + " " + registerUser.get("password") + " " + registerUser.get("phone") + " " + registerUser.get("name"));
        return accountService.registerUser(registerUser.get("email"), registerUser.get("password"), registerUser.get("phone"), registerUser.get("name")).toString();
    }

    @ApiOperation(value = "查询邮箱是否存在")
    @GetMapping("/hasEmail")
    public boolean hasEmail(String email){
        return accountService.getUserByEmail(email) != null;
    }

    @ApiOperation(value = "查询邮箱是否已验证")
    @GetMapping("/isEmailVerify")
    public boolean isEmailVerify(String email){
        return accountService.isEmailVerify(email);
    }

    @ApiOperation(value = "验证邮箱")
    @GetMapping("/verifyEmail")
    public String verifyEmail(String email, String verifyCode){
        return accountService.verifyEmail(email, verifyCode);
    }

}