package cn.edu.scau.acm.acmer.controller.api;

import cn.edu.scau.acm.acmer.entity.Contest;
import cn.edu.scau.acm.acmer.httpclient.BaseHttpClient;
import cn.edu.scau.acm.acmer.model.MyResponseEntity;
import cn.edu.scau.acm.acmer.repository.ContestRepository;
import cn.edu.scau.acm.acmer.repository.ProblemAcRecordRepository;
import cn.edu.scau.acm.acmer.service.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "api/test", produces = "application/json; charset=utf-8")
public class TestController {

    @Autowired
    ProblemService problemService;

    @Autowired
    ScauCfService scauCfService;

    @Autowired
    ProblemAcRecordRepository problemAcRecordRepository;

    @Autowired
    ContestService contestService;

    @Autowired
    ContestRepository contestRepository;

    @Autowired
    ScheduledService scheduledService;

    @GetMapping("/addAc")
    void addAc() {
        scheduledService.getAllAcProblemsFromOj();
    }

    @GetMapping("/addContest")
    MyResponseEntity<Void> addContest(String ojName, String cId, String username, String password) {
        try {
            BaseHttpClient httpClient = null;
            contestService.addContest(httpClient, ojName, cId, username, password);
            return new MyResponseEntity<>();
        } catch (Exception e) {
            return new MyResponseEntity<>(e.getMessage());
        }
    }

    @GetMapping("/addPersonalContestRecord")
    MyResponseEntity<Void> addPersonalContestRecord(String ojName, String cId, String account, String password, String studentId) {
        try {
            contestService.addPersonalContestRecord(ojName, cId, password, studentId, account);
            return new MyResponseEntity<>();
        } catch (Exception e) {
            return new MyResponseEntity<>(e.getMessage());
        }
    }

    @GetMapping("showContest")
    MyResponseEntity<Contest> showContest(String ojName, String ojId) {
        return new MyResponseEntity(contestRepository.findByOjNameAndCId(ojName, ojId));
    }

    @GetMapping("/testSele")
    void testSele() throws InterruptedException {
        WebDriver driver = new ChromeDriver();
        driver.get("https://passport.jisuanke.com/?n=https://www.jisuanke.com/contest/3007?view=rank#/");
        WebElement username = driver.findElements(By.tagName("input")).get(0);
        username.sendKeys("15914764919");
        WebElement password = driver.findElements(By.tagName("input")).get(1);
        password.sendKeys("QqSt631665391");
        WebElement submit = driver.findElement(By.linkText("登录"));
        submit.click();
        Thread.sleep(2000);
        String html = driver.getPageSource();
        System.out.println(html);
        driver.close();
    }

}
