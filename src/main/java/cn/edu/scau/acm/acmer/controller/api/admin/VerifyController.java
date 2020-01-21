package cn.edu.scau.acm.acmer.controller.api.admin;

import cn.edu.scau.acm.acmer.entity.User;
import cn.edu.scau.acm.acmer.model.User_Student;
import cn.edu.scau.acm.acmer.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Pageable;

@RestController
@RequestMapping(value = "/api/admin/verify", produces = "application/json; charset=utf-8")
public class VerifyController {
    @Autowired
    AccountService accountService;

    /**
     * get UsersList who is unverify
     * @param page
     * @param size
     * @return
     */
    @GetMapping("getUserUnverify")
    Page<User_Student> getUserUnverify(Integer page, Integer size){
        return accountService.getUserUnverify(page, size);
    }

    /**
     * Verify the account
     * @param id
     */
    @PostMapping("registerVerify")
    void registerVerify(Integer id){
        accountService.verifyAccount(id);
    }

    @PostMapping("deleteAccount")
    void deleteAccount(Integer id){
        accountService.deleteAccount(id);
    }


}
