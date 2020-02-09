package cn.edu.scau.acm.acmer.controller.api;

import cn.edu.scau.acm.acmer.model.MyResponseEntity;
import cn.edu.scau.acm.acmer.service.AccountService;
import cn.edu.scau.acm.acmer.service.UserService;
import io.swagger.annotations.ApiOperation;
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

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(value = "/auth"  ,produces = "application/json; charset=utf-8")
public class AuthController {

    @Autowired
    AccountService accountService;

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    UserService userService;

    @ApiOperation("登录")
    @PostMapping("/login")
    public MyResponseEntity<Void> login(String email, String password, HttpServletResponse response){
        log.info(email + " " + password);
        Subject subject = SecurityUtils.getSubject();
        try {
            UsernamePasswordToken token = new UsernamePasswordToken(email, password);
            subject.login(token);

            if(!accountService.isVerify(email)){
                return new MyResponseEntity<>("账号未通过审核，请等待管理员审核");
            }
            response.setHeader("token", (String) subject.getSession().getId());
            return new MyResponseEntity<>();
        } catch (AuthenticationException e) {
            log.error("User {} login fail, Reason:{}", email, e.getMessage());
            return new MyResponseEntity<>("邮箱名或密码错误");
        } catch (Exception e) {
            return new MyResponseEntity<>("网络错误");
        }
    }

    @ApiOperation("注册")
    @PostMapping("/register")
    public MyResponseEntity<Void> register(String email, String password, String phone, String name, String verifyCode, String grade, String studentId, String type){
        try {
            if (type.equals("教师")) {
                accountService.registerUser(email, password, phone, name, verifyCode);
            } else {
                accountService.registerStudent(email, password, phone, name, verifyCode, Integer.parseInt(grade), studentId);
            }
            return new MyResponseEntity<>();
        } catch (Exception e) {
            return new MyResponseEntity<>(e.getMessage());
        }
    }

    @ApiOperation("注销")
    @GetMapping("/logout")
    public MyResponseEntity<Void> logout() {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return new MyResponseEntity<>();
    }

    @ApiOperation("发送邮箱验证码到邮箱")
    @PostMapping("/sendVerifyEmailCode")
    public MyResponseEntity<Void> sendVerifyEmailCode(String email) {
        try {
            accountService.sendVerifyEmail(email);
            return new MyResponseEntity<>();
        } catch (Exception e) {
            return new MyResponseEntity<>(e.getMessage());
        }
    }

    @ApiOperation("忘记密码的时候发送验证码到邮箱")
    @PostMapping("/sendForgetPasswordVerifyEmailCode")
    public MyResponseEntity<Void> sendForgetPasswordVerifyEmailCode(String email){
        try {
            accountService.sendForgetPasswordVerifyEmail(email);
            return new MyResponseEntity<>();
        } catch (Exception e) {
            return new MyResponseEntity<>(e.getMessage());
        }
    }

    @ApiOperation("忘记密码的时候修改密码")
    @PostMapping(value = "/forgetPassword")
    public MyResponseEntity<Void> forgetPassword(String email, String password, String verifyCode){
        try {
            accountService.forgetPassword(email, password, verifyCode);
            return new MyResponseEntity<>();
        } catch (Exception e) {
            return new MyResponseEntity<>(e.getMessage());
        }
    }

    @ApiOperation("权限不足时返回401")
    @RequestMapping(value = "/unauth")
    @ResponseBody
    public ResponseEntity unauth() {
        return new ResponseEntity(HttpStatus.UNAUTHORIZED);
    }

}