package cn.edu.scau.acm.acmer.controller.api.common;

import cn.edu.scau.acm.acmer.model.MyResponseEntity;
import cn.edu.scau.acm.acmer.model.UserDto;
import cn.edu.scau.acm.acmer.model.User_Student;
import cn.edu.scau.acm.acmer.service.AccountService;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/common/user", produces = "application/json; charset=utf-8")
public class UserController {
    @Autowired
    AccountService accountService;

    @ApiOperation("获取目前登录用户的信息")
    @GetMapping("getMyInfo")
    MyResponseEntity<User_Student> getMyInfo() {
        int id = ((UserDto) SecurityUtils.getSubject().getPrincipal()).getId();
        try {
            return new MyResponseEntity<>(accountService.getUserStudentById(id));
        } catch (Exception e) {
            return new MyResponseEntity<>(e.getMessage());
        }
    }

    @ApiOperation("修改当前登录用户的个人信息，包括手机号码和icpc邮箱")
    @PostMapping("changeMyInfo")
    MyResponseEntity<Void> changeMyInfo(String phone, String icpcEmail) {
        int id = ((UserDto) SecurityUtils.getSubject().getPrincipal()).getId();
        try {
            accountService.changePhoneAndIcpcEmail(phone, icpcEmail, id);
            return new MyResponseEntity<>();
        } catch (Exception e) {
            return new MyResponseEntity<>(e.getMessage());
        }
    }
}
