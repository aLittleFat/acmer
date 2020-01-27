package cn.edu.scau.acm.acmer.controller.api.student;

import cn.edu.scau.acm.acmer.model.UserDto;
import cn.edu.scau.acm.acmer.service.OJAccountService;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api/student/ojaccount", produces = "application/json; charset=utf-8")
public class OJAccountController {
    @Autowired
    OJAccountService ojAccountService;

    /**
     * add vj account
     * @param username
     * @param password
     * @return
     */
    @PostMapping("addMyOjAccount")
    String addMyOjAccount(String ojName, String username, String password){
        int userId = ((UserDto) SecurityUtils.getSubject().getPrincipal()).getId();
        return ojAccountService.addOjAccount(ojName, username, password, userId);
    }

    @GetMapping("getMyOjAccount")
    String getMyOjAccount(String ojName){
        int userId = ((UserDto) SecurityUtils.getSubject().getPrincipal()).getId();
        return ojAccountService.getOjAccount(ojName, userId);
    }

    @PostMapping("changeMyOjAccount")
    String changeMyOjAccount(String ojName, String username, String password){
        int userId = ((UserDto) SecurityUtils.getSubject().getPrincipal()).getId();
        return ojAccountService.changeOjAccount(ojName, username, password, userId);
    }

    @PostMapping("deleteMyOjAccount")
    String deleteMyOjAccount(String ojName){
        int userId = ((UserDto) SecurityUtils.getSubject().getPrincipal()).getId();
        return ojAccountService.deleteOjAccount(ojName, userId);
    }
}
