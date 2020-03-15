package cn.edu.scau.acm.acmer.controller.api;

import cn.edu.scau.acm.acmer.entity.User;
import cn.edu.scau.acm.acmer.model.MyResponseEntity;
import cn.edu.scau.acm.acmer.service.AccountService;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api", produces = "application/json; charset=utf-8")
public class VerifyController {
    @Autowired
    AccountService accountService;

    @ApiOperation("获取未通过注册审核的用户信息")
    @GetMapping("user_unVerify")
    MyResponseEntity<Page<User>> getUserUnverify(Integer page, Integer size){
        return new MyResponseEntity<>(accountService.getUserUnVerified(page, size));
    }

    @ApiOperation("获取申请退役的队员列表")
    @RequiresRoles("admin")
    @GetMapping("requestRetireUser")
    MyResponseEntity<Page<User>> getRequestRetireUser(Integer page, Integer size){
        return new MyResponseEntity<>(accountService.getRequestRetireUser(page, size));
    }

    @ApiOperation("通过注册审核")
    @PutMapping("registerVerify")
    @RequiresRoles({"admin"})
    MyResponseEntity<Void> registerVerify(Integer id) throws Exception {
        accountService.verifyAccount(id);
        return new MyResponseEntity<>();
    }

    @ApiOperation("不通过注册审核，并删除账户")
    @DeleteMapping("user")
    @RequiresRoles({"admin"})
    MyResponseEntity<Void> deleteAccount(Integer id) {
        accountService.deleteAccount(id);
        return new MyResponseEntity<>();
    }

    @ApiOperation("修改队员状态")
    @PutMapping("user/{id}/status")
    @RequiresRoles("admin")
    MyResponseEntity<Void> changeUserStatus(@PathVariable Integer id, String status) {
        accountService.changeUserStatus(id, status);
        return new MyResponseEntity<>();
    }


}
