package cn.edu.scau.acm.acmer.controller.api.student;

import cn.edu.scau.acm.acmer.service.OJAccountService;
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
    @PostMapping("addVjAccount")
    String addVjAccount(String username, String password, int id){
        return ojAccountService.addVjAccount(username, password, id);
    }
}
