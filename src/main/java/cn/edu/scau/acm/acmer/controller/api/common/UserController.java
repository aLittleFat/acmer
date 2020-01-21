package cn.edu.scau.acm.acmer.controller.api.common;

import cn.edu.scau.acm.acmer.entity.User;
import cn.edu.scau.acm.acmer.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/common/user")
public class UserController {
    @Autowired
    AccountService accountService;

    @RequestMapping(method = RequestMethod.GET, value = "/getUser", produces = "application/json; charset=utf-8")
    User getUser(int id){
        return accountService.getUserById(id);
    }
}
