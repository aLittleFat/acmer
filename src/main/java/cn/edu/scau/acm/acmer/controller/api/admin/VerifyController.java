package cn.edu.scau.acm.acmer.controller.api.admin;

import cn.edu.scau.acm.acmer.model.MyResponseEntity;
import cn.edu.scau.acm.acmer.model.User_Student;
import cn.edu.scau.acm.acmer.service.AccountService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/admin/verify", produces = "application/json; charset=utf-8")
public class VerifyController {
    @Autowired
    AccountService accountService;

    @ApiOperation("获取未通过注册审核的用户信息")
    @GetMapping("getUserUnverify")
    MyResponseEntity<Page<User_Student>> getUserUnverify(Integer page, Integer size){
        try {
            return new MyResponseEntity<>(accountService.getUserUnverify(page, size));
        } catch (Exception e) {
            return new MyResponseEntity<>(e.getMessage());
        }
    }

    @ApiOperation("通过注册审核")
    @PostMapping("registerVerify")
    MyResponseEntity<Void> registerVerify(Integer id){
        try {
            accountService.verifyAccount(id);
            return new MyResponseEntity<>();
        } catch (Exception e) {
            return new MyResponseEntity<>(e.getMessage());
        }
    }

    @ApiOperation("不通过注册审核，并删除账户")
    @PostMapping("deleteAccount")
    MyResponseEntity<Void> deleteAccount(Integer id) {
        try{
            accountService.deleteAccount(id);
            return new MyResponseEntity<>();
        } catch (Exception e) {
            return new MyResponseEntity<>(e.getMessage());
        }
    }


}
