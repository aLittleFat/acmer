package cn.edu.scau.acm.acmer.controller.api.unauth;

import cn.edu.scau.acm.acmer.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "api/unauth/test", produces = "application/json; charset=utf-8")
public class TestController {

    @Autowired
    ProblemService problemService;

    @GetMapping("/test")
    void test(){
        problemService.getAllAcProblemsFromOj();
    }

}
