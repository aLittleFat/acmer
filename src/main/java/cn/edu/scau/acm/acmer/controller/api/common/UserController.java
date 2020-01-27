package cn.edu.scau.acm.acmer.controller.api.common;

import cn.edu.scau.acm.acmer.entity.User;
import cn.edu.scau.acm.acmer.model.UserDto;
import cn.edu.scau.acm.acmer.model.User_Student;
import cn.edu.scau.acm.acmer.service.AccountService;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/common/user", produces = "application/json; charset=utf-8")
public class UserController {
    @Autowired
    AccountService accountService;

    @GetMapping("getMyInfo")
    User_Student getMyInfo() {
        int id = ((UserDto) SecurityUtils.getSubject().getPrincipal()).getId();
        return accountService.getUserStudentById(id);
    }

    @PostMapping("changeMyInfo")
    String changeMyInfo(String phone, String icpcEmail) {
        int id = ((UserDto) SecurityUtils.getSubject().getPrincipal()).getId();
        return accountService.changePhoneAndIcpcEmail(phone, icpcEmail, id);
    }
}
