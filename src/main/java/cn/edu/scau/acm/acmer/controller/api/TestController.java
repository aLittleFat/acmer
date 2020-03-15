package cn.edu.scau.acm.acmer.controller.api;

import cn.edu.scau.acm.acmer.entity.Contest;
import cn.edu.scau.acm.acmer.model.*;
import cn.edu.scau.acm.acmer.repository.ContestRecordRepository;
import cn.edu.scau.acm.acmer.repository.ContestRepository;
import cn.edu.scau.acm.acmer.repository.ProblemAcRecordRepository;
import cn.edu.scau.acm.acmer.repository.UserRepository;
import cn.edu.scau.acm.acmer.service.*;
import com.alibaba.fastjson.JSONObject;
import org.apache.shiro.SecurityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;


@RestController
@RequestMapping(value = "api/test", produces = "application/json; charset=utf-8")
public class TestController {

    Logger log = LoggerFactory.getLogger(this.getClass());

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

    @Autowired
    OpenTrainsService openTrainsService;

    @Autowired
    ContestRecordRepository contestRecordRepository;

    @Autowired
    UserRepository userRepository;


    @GetMapping("/addAc")
    void addAc() {
        scheduledService.getAllAcProblemsFromOj();
    }

//    @GetMapping("/addContest")
//    MyResponseEntity<Void> addContest(String ojName, String cId, String username, String password) {
//        try {
//            BaseHttpClient httpClient = null;
//            contestService.addContest(httpClient, ojName, cId, username, password);
//            return new MyResponseEntity<>();
//        } catch (Exception e) {
//            return new MyResponseEntity<>(e.getMessage());
//        }
//    }

    @GetMapping("/addPersonalContestRecord")
    MyResponseEntity<Void> addPersonalContestRecord(String ojName, String cId, String account, String password, String studentId) throws Exception {
        contestService.addContestRecord(ojName, cId, studentId, null, account, password);
        return new MyResponseEntity<>();
    }

    @GetMapping("showContest")
    MyResponseEntity<Contest> showContest(String ojName, String ojId) {
        return new MyResponseEntity(contestRepository.findByOjNameAndCid(ojName, ojId));
    }

    @GetMapping("/testSele")
    void testSele() throws InterruptedException {
        WebDriver driver = new ChromeDriver();
        driver.get("https://passport.jisuanke.com/?n=https://www.jisuanke.com/contest/3007?view=rank&page=1&school=%E5%8D%8E%E5%8D%97%E5%86%9C%E4%B8%9A%E5%A4%A7%E5%AD%A6#/");
        WebElement username = driver.findElements(By.tagName("input")).get(0);
        username.sendKeys("15914764919");
        WebElement password = driver.findElements(By.tagName("input")).get(1);
        password.sendKeys("QqSt631665391");
        WebElement submit = driver.findElement(By.linkText("登录"));
        submit.click();
        Thread.sleep(2000);
        Document document = Jsoup.parse(driver.getPageSource());
        Elements table = document.selectFirst("tbody").select("tr");
        for (Element tr : table) {
            Elements tds = tr.select("td");
            log.info(tds.text());
        }
        driver.close();
    }

    @GetMapping("/OpenTrains")
    void testOpenTrains(String username) throws Exception {
        openTrainsService.login(username);
    }

    @GetMapping("/testCharts")
    MyResponseEntity<List<OjAcChart>> testCharts(String studentId){
        return new MyResponseEntity<>(problemAcRecordRepository.countAllByStudentIdGroupByOJ(studentId));
    }

}
