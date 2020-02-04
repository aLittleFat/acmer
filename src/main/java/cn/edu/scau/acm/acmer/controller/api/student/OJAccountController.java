package cn.edu.scau.acm.acmer.controller.api.student;

import cn.edu.scau.acm.acmer.model.UserDto;
import cn.edu.scau.acm.acmer.service.OJAccountService;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "api/student/ojaccount", produces = "application/json; charset=utf-8")
public class OJAccountController {
    @Autowired
    OJAccountService ojAccountService;

    @ApiOperation("添加OJ账户")
    @PostMapping("addMyOjAccount")
    String addMyOjAccount(String ojName, String username, String password){
        int userId = ((UserDto) SecurityUtils.getSubject().getPrincipal()).getId();
        return ojAccountService.addOjAccount(ojName, username, password, userId);
    }

    @ApiOperation("获取登录账户的OJ账户")
    @GetMapping("getMyOjAccount")
    String getMyOjAccount(String ojName){
        int userId = ((UserDto) SecurityUtils.getSubject().getPrincipal()).getId();
        return ojAccountService.getOjAccount(ojName, userId);
    }

    @ApiOperation("修改登录账户的OJ账户")
    @PostMapping("changeMyOjAccount")
    String changeMyOjAccount(String ojName, String username, String password){
        int userId = ((UserDto) SecurityUtils.getSubject().getPrincipal()).getId();
        return ojAccountService.changeOjAccount(ojName, username, password, userId);
    }

    @ApiOperation("删除登录账户的OJ账户")
    @PostMapping("deleteMyOjAccount")
    String deleteMyOjAccount(String ojName){
        int userId = ((UserDto) SecurityUtils.getSubject().getPrincipal()).getId();
        return ojAccountService.deleteOjAccount(ojName, userId);
    }
}
