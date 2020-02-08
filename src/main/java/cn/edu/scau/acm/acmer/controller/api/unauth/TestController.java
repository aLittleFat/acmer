package cn.edu.scau.acm.acmer.controller.api.unauth;

import cn.edu.scau.acm.acmer.model.PersonalProblemAcRank;
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

import java.util.List;


@RestController
@RequestMapping(value = "api/unauth/test", produces = "application/json; charset=utf-8")
public class TestController {

    @Autowired
    ProblemService problemService;

    @Autowired
    ScauCfService scauCfService;

    @Autowired
    ProblemAcRecordRepository problemAcRecordRepository;

    @GetMapping("/addAc")
    void addAc() {
        problemService.getAllAcProblemsFromOj();
    }

    @GetMapping("/test")
    List<PersonalProblemAcRank> test(){

        return problemService.getPersonalProblemAcRank(0, false);
    }

    @GetMapping("/testSele")
    void testSele() throws InterruptedException {
        System.setProperty("webdriver.chrome.driver", "C:\\Driver\\chromedriver.exe");
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
