package cn.edu.scau.acm.acmer.controller.api.student;

import cn.edu.scau.acm.acmer.model.MyResponseEntity;
import cn.edu.scau.acm.acmer.model.UserDto;
import cn.edu.scau.acm.acmer.service.OJAccountService;
import cn.edu.scau.acm.acmer.service.ScauCfService;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "api/student/ojAccount", produces = "application/json; charset=utf-8")
public class OJAccountController {

    @Autowired
    private OJAccountService ojAccountService;

    @Autowired
    private ScauCfService scauCfService;

    @ApiOperation("添加OJ账户")
    @PostMapping("addMyOjAccount")
    MyResponseEntity<Void> addMyOjAccount(String ojName, String username, String password){
        int userId = ((UserDto) SecurityUtils.getSubject().getPrincipal()).getId();
        try {
            ojAccountService.addOjAccount(ojName, username, password, userId);
            return new MyResponseEntity<>();
        } catch (Exception e) {
            return new MyResponseEntity<>(e.getMessage());
        }
    }

    @ApiOperation("获取登录账户的OJ账户")
    @GetMapping("getMyOjAccount")
    MyResponseEntity<String> getMyOjAccount(String ojName){
        int userId = ((UserDto) SecurityUtils.getSubject().getPrincipal()).getId();
        return new MyResponseEntity<>(0, "", ojAccountService.getOjAccount(ojName, userId));
    }

    @ApiOperation("修改登录账户的OJ账户")
    @PostMapping("changeMyOjAccount")
    MyResponseEntity<Void> changeMyOjAccount(String ojName, String username, String password){
        int userId = ((UserDto) SecurityUtils.getSubject().getPrincipal()).getId();
        try {
            ojAccountService.changeOjAccount(ojName, username, password, userId);
            return new MyResponseEntity<>();
        } catch (Exception e) {
            return new MyResponseEntity<>(e.getMessage());
        }
    }

    @ApiOperation("删除登录账户的OJ账户")
    @PostMapping("deleteMyOjAccount")
    MyResponseEntity<Void> deleteMyOjAccount(String ojName){
        int userId = ((UserDto) SecurityUtils.getSubject().getPrincipal()).getId();
        try {
            ojAccountService.deleteOjAccount(ojName, userId);
            return new MyResponseEntity<>();
        } catch (Exception e) {
            return new MyResponseEntity<>(e.getMessage());
        }
    }

    @PostMapping("sendCfVerifyCode")
    MyResponseEntity<Void> sendCfVerifyCode(String username) {
        try {
            scauCfService.sendCfVerifyCode(username);
            return new MyResponseEntity<>();
        } catch (Exception e) {
            return new MyResponseEntity<>(e.getMessage());
        }
    }
}
