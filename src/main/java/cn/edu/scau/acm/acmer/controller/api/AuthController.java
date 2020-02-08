package cn.edu.scau.acm.acmer.controller.api;

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
import org.springframework.http.HttpEntity;
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
    public ResponseEntity<String> login(String email, String password, HttpServletResponse response){
        log.info(email + " " + password);
        Subject subject = SecurityUtils.getSubject();
        try {
            UsernamePasswordToken token = new UsernamePasswordToken(email, password);
            subject.login(token);

            if(!accountService.isVerify(email)){
                return new ResponseEntity<String>("账号未通过审核，请等待管理员审核", HttpStatus.OK);
            }
            response.setHeader("token", (String) subject.getSession().getId());
            return new ResponseEntity<String>("true", HttpStatus.OK);
        } catch (AuthenticationException e) {
            log.error("User {} login fail, Reason:{}", email, e.getMessage());
            return new ResponseEntity<String>("邮箱名或密码错误", HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<String>("网络错误", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation("注册")
    @PostMapping("/register")
    public String register(String email, String password, String phone, String name, String verifyCode, String grade, String studentId, String type){
        if(type.equals("教师")) {
            return accountService.registerUser(email, password, phone, name, verifyCode);
        }
        else {
            return accountService.registerStudent(email, password, phone, name, verifyCode,Integer.parseInt(grade), studentId);
        }
    }

    @ApiOperation("注销")
    @GetMapping("/logout")
    public ResponseEntity<Void> logout() {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return ResponseEntity.ok().build();
    }

    @ApiOperation("验证邮箱")
    @GetMapping("/verifyEmail")
    public String verifyEmail(String email, String verifyCode){
        return accountService.verifyEmail(email, verifyCode);
    }

    @ApiOperation("发送邮箱验证码到邮箱")
    @PostMapping("/sendVerifyEmailCode")
    public String sendVerifyEmailCode(String email){
        return accountService.sendVerifyEmail(email);
    }

    @ApiOperation("忘记密码的时候发送验证码到邮箱")
    @PostMapping("/sendForgetPasswordVerifyEmailCode")
    public String sendForgetPasswordVerifyEmailCode(String email){
        return accountService.sendForgetPasswordVerifyEmail(email);
    }

    @ApiOperation("忘记密码的时候修改密码")
    @PostMapping(value = "/forgetPassword")
    public String forgetPassword(String email, String password, String verifyCode){
        return accountService.forgetPassword(email, password, verifyCode);
    }

    @ApiOperation("权限不足时返回401")
    @RequestMapping(value = "/unauth")
    @ResponseBody
    public HttpEntity<Void> unauth() {
        return new ResponseEntity(HttpStatus.UNAUTHORIZED);
    }

}