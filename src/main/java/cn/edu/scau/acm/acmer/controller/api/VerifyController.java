package cn.edu.scau.acm.acmer.controller.api;

import cn.edu.scau.acm.acmer.entity.AwardView;
import cn.edu.scau.acm.acmer.entity.User;
import cn.edu.scau.acm.acmer.model.MyResponseEntity;
import cn.edu.scau.acm.acmer.service.AccountService;
import cn.edu.scau.acm.acmer.service.AwardService;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api", produces = "application/json; charset=utf-8")
public class VerifyController {
    @Autowired
    private AccountService accountService;

    @Autowired
    private AwardService awardService;

    @ApiOperation("获取未通过注册审核的用户信息")
    @RequiresRoles("admin")
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

    @ApiOperation("获取待审核的获奖记录")
    @RequiresRoles("admin")
    @GetMapping("award/notVerified")
    MyResponseEntity<Page<AwardView>> getAwardViewNotVerified(Integer page, Integer size) {
        return new MyResponseEntity<>(awardService.getAwardViewNotVerified(page, size));
    }

    @ApiOperation("通过审核获奖记录")
    @RequiresRoles("admin")
    @PutMapping("award/{awardId}/verified")
    MyResponseEntity<Void> verifyAward(@PathVariable Integer awardId) throws Exception {
        awardService.verifyAward(awardId);
        return new MyResponseEntity<>();
    }

    @ApiOperation("不通过获奖记录，删除记录并对队伍发送邮件通知")
    @RequiresRoles("admin")
    @DeleteMapping("award/{awardId}/verified")
    MyResponseEntity<Void> deleteAward(@PathVariable Integer awardId) throws Exception {
        awardService.deleteAward(awardId);
        return new MyResponseEntity<>();
    }

}
