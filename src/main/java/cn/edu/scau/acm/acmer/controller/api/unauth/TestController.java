package cn.edu.scau.acm.acmer.controller.api.unauth;

import cn.edu.scau.acm.acmer.service.BzojService;
import cn.edu.scau.acm.acmer.service.CfService;
import cn.edu.scau.acm.acmer.service.HduService;
import cn.edu.scau.acm.acmer.service.VjService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "api/unauth/test", produces = "application/json; charset=utf-8")
public class TestController {

    @Autowired
    CfService cfService;

    @GetMapping("/test")
    void test(){
        cfService.getAllAcProblems();
    }

}
