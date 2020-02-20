package cn.edu.scau.acm.acmer.controller.api;

import cn.edu.scau.acm.acmer.entity.User;
import cn.edu.scau.acm.acmer.model.MyResponseEntity;
import cn.edu.scau.acm.acmer.model.UserDto;
import cn.edu.scau.acm.acmer.repository.UserRepository;
import cn.edu.scau.acm.acmer.service.AccountService;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api", produces = "application/json; charset=utf-8")
public class UserController {
    @Autowired
    AccountService accountService;

    @Autowired
    private UserRepository userRepository;

    @ApiOperation("获取目前登录用户的信息")
    @RequiresAuthentication()
    @GetMapping("info")
    MyResponseEntity<User> getMyInfo() {
        int id = ((UserDto) SecurityUtils.getSubject().getPrincipal()).getId();
        return new MyResponseEntity<>(userRepository.findById(id).get());
    }

    @ApiOperation("修改当前登录用户的个人信息，包括手机号码和icpc邮箱")
    @RequiresRoles({"student"})
    @PutMapping("info")
    MyResponseEntity<Void> changeMyInfo(String phone, String icpcEmail) {
        int id = ((UserDto) SecurityUtils.getSubject().getPrincipal()).getId();
        accountService.changePhoneAndIcpcEmail(phone, icpcEmail, id);
        return new MyResponseEntity<>();
    }
}
