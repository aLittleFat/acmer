package cn.edu.scau.acm.acmer.controller.api;

import cn.edu.scau.acm.acmer.entity.User;
import cn.edu.scau.acm.acmer.model.MyResponseEntity;
import cn.edu.scau.acm.acmer.model.UserDto;
import cn.edu.scau.acm.acmer.repository.UserRepository;
import cn.edu.scau.acm.acmer.service.AccountService;
import cn.edu.scau.acm.acmer.service.UserService;
import com.alibaba.fastjson.JSONObject;
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
@RequestMapping(value = "/api/auth"  ,produces = "application/json; charset=utf-8")
public class AuthController {

    @Autowired
    AccountService accountService;

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

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

    @GetMapping("roles")
    public MyResponseEntity<JSONObject> roles() {
        JSONObject roles = new JSONObject();
        try {
            int id = ((UserDto) SecurityUtils.getSubject().getPrincipal()).getId();
            User user = userRepository.findById(id).get();
            if(user.getStudentId() != null) {
                roles.put("is_student", true);
            } else {
                roles.put("is_student", false);
            }
            if(user.getIsAdmin() == (byte)1) {
                roles.put("is_admin", true);
            } else {
                roles.put("is_admin", false);
            }
        } catch (Exception e) {
            roles.put("is_student", false);
            roles.put("is_admin", false);
        }
        return new MyResponseEntity<>(roles);
    }

    @ApiOperation("注册")
    @PostMapping("/register")
    public MyResponseEntity<Void> register(String email, String password, String phone, String name, String verifyCode, String grade, String studentId, String qq, String type) throws Exception {
        if (type.equals("教师")) {
            studentId = null;
        }
        accountService.register(email, password, phone, name, verifyCode, Integer.parseInt(grade), studentId, qq);
        return new MyResponseEntity<>();
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
    public MyResponseEntity<Void> sendVerifyEmailCode(String email) throws Exception {
        accountService.sendVerifyEmail(email);
        return new MyResponseEntity<>();
    }

    @ApiOperation("忘记密码的时候发送验证码到邮箱")
    @PostMapping("/sendForgetPasswordVerifyEmailCode")
    public MyResponseEntity<Void> sendForgetPasswordVerifyEmailCode(String email) throws Exception {
        accountService.sendForgetPasswordVerifyEmail(email);
        return new MyResponseEntity<>();
    }

    @ApiOperation("忘记密码的时候修改密码")
    @PostMapping(value = "/forgetPassword")
    public MyResponseEntity<Void> forgetPassword(String email, String password, String verifyCode) throws Exception {
        accountService.forgetPassword(email, password, verifyCode);
        return new MyResponseEntity<>();
    }

    @ApiOperation("权限不足时返回401")
    @GetMapping(value = "/unauth")
    public ResponseEntity unauth() {
        return new ResponseEntity(HttpStatus.UNAUTHORIZED);
    }

}