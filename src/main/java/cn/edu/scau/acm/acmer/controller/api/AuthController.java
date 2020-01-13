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

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    UserService userService;

    /**
     * Login
     * @param email
     * @param password
     * @param response
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/login", produces = "application/json; charset=utf-8")
    public ResponseEntity<String> login(String email, String password, HttpServletResponse response){
        log.info(email + " " + password);
        Subject subject = SecurityUtils.getSubject();
        try {
            UsernamePasswordToken token = new UsernamePasswordToken(email, password);
            subject.login(token);

            if(!accountService.isVerify(email)){
                return new ResponseEntity<String>("账号未通过审核，请等待管理员审核", HttpStatus.OK);
            }

            UserDto user = (UserDto) subject.getPrincipal();
            String newToken = userService.generateJwtToken(user.getUsername());
            response.setHeader("token", newToken);

            return new ResponseEntity<String>("true", HttpStatus.OK);
        } catch (AuthenticationException e) {
            log.error("User {} login fail, Reason:{}", email, e.getMessage());
            return new ResponseEntity<String>("邮箱名或密码错误", HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<String>("网络错误", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * register
     * @param email
     * @param password
     * @param phone
     * @param name
     * @param verifyCode
     * @param grade
     * @param studentId
     * @param type
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/register", produces = "application/json; charset=utf-8")
    public String register(String email, String password, String phone, String name, String verifyCode, String grade, String studentId, String type){
        if(type.equals("教师")) {
            return accountService.registerUser(email, password, phone, name, verifyCode);
        }
        else {
            return accountService.registerStudent(email, password, phone, name, verifyCode,Integer.parseInt(grade), studentId);
        }
    }

    /**
     * 退出登录
     * @return
     */
    @GetMapping(value = "/logout")
    public ResponseEntity<Void> logout() {
        Subject subject = SecurityUtils.getSubject();
        if(subject.getPrincipals() != null) {
            UserDto user = (UserDto)subject.getPrincipals().getPrimaryPrincipal();
            userService.deleteLoginInfo(user.getUsername());
        }
        SecurityUtils.getSubject().logout();
        return ResponseEntity.ok().build();
    }

    /**
     * VerifyEmail
     * @param email
     * @param verifyCode
     * @return
     */
    @GetMapping("/verifyEmail")
    public String verifyEmail(String email, String verifyCode){
        return accountService.verifyEmail(email, verifyCode);
    }

    /**
     * Send the Verify Email Code
     * @param email
     */
    @RequestMapping(method = RequestMethod.POST, value = "/sendVerifyEmailCode", produces = "application/json; charset=utf-8")
    public String sendVerifyEmailCode(String email){
        return accountService.sendVerifyEmail(email);
    }

    /**
     * Send the Verify Email Code when Forget Password
     * @param email
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/sendForgetPasswordVerifyEmailCode", produces = "application/json; charset=utf-8")
    public String sendForgetPasswordVerifyEmailCode(String email){
        return accountService.sendForgetPasswordVerifyEmail(email);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/forgetPassword", produces = "application/json; charset=utf-8")
    public String forgetPassword(String email, String password, String verifyCode){
        return accountService.forgetPassword(email, password, verifyCode);
    }

}