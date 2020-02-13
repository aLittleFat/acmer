package cn.edu.scau.acm.acmer.controller.api;

import cn.edu.scau.acm.acmer.model.MyResponseEntity;
import cn.edu.scau.acm.acmer.model.UserDto;
import cn.edu.scau.acm.acmer.service.OJAccountService;
import cn.edu.scau.acm.acmer.service.ScauCfService;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api", produces = "application/json; charset=utf-8")
public class OJAccountController {

    @Autowired
    private OJAccountService ojAccountService;

    @Autowired
    private ScauCfService scauCfService;

    @ApiOperation("添加OJ账户")
    @RequiresRoles({"student"})
    @PostMapping("ojAccount")
    MyResponseEntity<Void> addMyOjAccount(String ojName, String username, String password) throws Exception {
        int userId = ((UserDto) SecurityUtils.getSubject().getPrincipal()).getId();
        ojAccountService.addOjAccount(ojName, username, password, userId);
        return new MyResponseEntity<>();
    }

    @ApiOperation("获取登录账户的OJ账户")
    @RequiresRoles({"student"})
    @GetMapping("ojAccount")
    MyResponseEntity<String> getMyOjAccount(String ojName){
        int userId = ((UserDto) SecurityUtils.getSubject().getPrincipal()).getId();
        return new MyResponseEntity<>(0, "", ojAccountService.getOjAccount(ojName, userId));
    }

    @ApiOperation("修改登录账户的OJ账户")
    @RequiresRoles({"student"})
    @PutMapping("ojAccount")
    MyResponseEntity<Void> changeMyOjAccount(String ojName, String username, String password) throws Exception {
        int userId = ((UserDto) SecurityUtils.getSubject().getPrincipal()).getId();
        ojAccountService.changeOjAccount(ojName, username, password, userId);
        return new MyResponseEntity<>();
    }

    @ApiOperation("删除登录账户的OJ账户")
    @RequiresRoles({"student"})
    @DeleteMapping("ojAccount")
    MyResponseEntity<Void> deleteMyOjAccount(String ojName) throws Exception {
        int userId = ((UserDto) SecurityUtils.getSubject().getPrincipal()).getId();
        ojAccountService.deleteOjAccount(ojName, userId);
        return new MyResponseEntity<>();

    }

    @ApiOperation("发送CF验证码")
    @RequiresRoles({"student"})
    @PostMapping("cfVerifyCode")
    MyResponseEntity<Void> sendCfVerifyCode(String username) throws Exception {
        scauCfService.sendCfVerifyCode(username);
        return new MyResponseEntity<>();
    }
}
