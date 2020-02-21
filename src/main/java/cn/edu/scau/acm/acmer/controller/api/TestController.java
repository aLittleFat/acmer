package cn.edu.scau.acm.acmer.controller.api;

import cn.edu.scau.acm.acmer.entity.Contest;
import cn.edu.scau.acm.acmer.httpclient.BaseHttpClient;
import cn.edu.scau.acm.acmer.model.MyResponseEntity;
import cn.edu.scau.acm.acmer.repository.ContestRepository;
import cn.edu.scau.acm.acmer.repository.ProblemAcRecordRepository;
import cn.edu.scau.acm.acmer.service.*;
import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
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

import java.util.ArrayList;
import java.util.List;


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

//    @GetMapping("/testJisuanke")
//    void testJisuanke() throws Exception {
//        BaseHttpClient httpClient = new BaseHttpClient();
//        httpClient.get("https://www.jisuanke.com/");
//        String xsrf = httpClient.getCookie("XSRF-TOKEN");
//        List<NameValuePair> params = new ArrayList<>();
//        params.add(new BasicNameValuePair("account", "+8615914764919"));
//        params.add(new BasicNameValuePair("pwd", "3a54c27831354a122c2b69dce1efe599"));
//        params.add(new BasicNameValuePair("save", "1"));
//        List<Header> headers = new ArrayList<>();
//        headers.add(new BasicHeader("XSRF-TOKEN", xsrf));
//        headers.add(new BasicHeader("Accept", "application/json, text/javascript, */*; q=0.01"));
//        headers.add(new BasicHeader("X-Requested-With", "XMLHttpRequest"));
//        headers.add(new BasicHeader("Host", "passport.jisuanke.com"));
//        headers.add(new BasicHeader("Origin", "https://passport.jisuanke.com"));
//        headers.add(new BasicHeader("Referer", "https://passport.jisuanke.com/?n=https://www.jisuanke.com/"));
//        headers.add(new BasicHeader("Sec-Fetch-Dest", "empty"));
//        headers.add(new BasicHeader("Sec-Fetch-Mode", "cors"));
//        headers.add(new BasicHeader("Sec-Fetch-Site", "same-origin"));
//        headers.add(new BasicHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.116 Safari/537.36"));
//        httpClient.post("https://passport.jisuanke.com/auth/login", params, headers);
//        log.info(httpClient.get("https://www.jisuanke.com/"));
//    }

}
