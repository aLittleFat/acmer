package cn.edu.scau.acm.acmer.controller.api;

import cn.edu.scau.acm.acmer.model.UserDto;
import cn.edu.scau.acm.acmer.service.AccountService;
import cn.edu.scau.acm.acmer.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    AccountService accountService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    UserService userService;

    /**
     * Login
     * @param email
     * @param password
     * @param response
     * @return
     */
    @PostMapping(value = "/login")
    public ResponseEntity<Void> login(String email, String password, HttpServletResponse response){
        Subject subject = SecurityUtils.getSubject();
        try {
            UsernamePasswordToken token = new UsernamePasswordToken(email, password);
            subject.login(token);

            UserDto user = (UserDto) subject.getPrincipal();
            String newToken = userService.generateJwtToken(user.getUsername());
            response.setHeader("x-auth-token", newToken);

            return ResponseEntity.ok().build();
        } catch (AuthenticationException e) {
            logger.error("User {} login fail, Reason:{}", email, e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    /**
     * Teacher Register
     * @param email
     * @param password
     * @param phone
     * @param name
     * @return
     */
    @PostMapping("/register/teacher")
    public String registerTeacher(String email, String password, String phone, String name){
        System.out.println(email + " " + password + " " + phone + " " + name);
        return accountService.registerUser(email, password, phone, name).toString();
    }

    /**
     * Student Register
     * @param registerUser
     * @return
     */
    @PostMapping("/register/student")
    public String registerStudent(@RequestBody Map<String,String> registerUser){
        System.out.println(registerUser.get("email") + " " + registerUser.get("password") + " " + registerUser.get("phone") + " " + registerUser.get("name"));
        return accountService.registerUser(registerUser.get("email"), registerUser.get("password"), registerUser.get("phone"), registerUser.get("name")).toString();
    }

    /**
     * 验证邮箱
     * @param email
     * @param verifyCode
     * @return
     */
    @GetMapping("/verifyEmail")
    public String verifyEmail(String email, String verifyCode){
        return accountService.verifyEmail(email, verifyCode);
    }

}