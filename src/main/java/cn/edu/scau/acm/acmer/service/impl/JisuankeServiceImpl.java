package cn.edu.scau.acm.acmer.service.impl;

import cn.edu.scau.acm.acmer.entity.Contest;
import cn.edu.scau.acm.acmer.entity.ContestProblemRecord;
import cn.edu.scau.acm.acmer.entity.ContestRecord;
import cn.edu.scau.acm.acmer.repository.ContestProblemRecordRepository;
import cn.edu.scau.acm.acmer.repository.ContestRecordRepository;
import cn.edu.scau.acm.acmer.repository.ContestRepository;
import cn.edu.scau.acm.acmer.service.JisuankeService;
import cn.edu.scau.acm.acmer.service.OJService;
import com.alibaba.fastjson.JSONObject;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class JisuankeServiceImpl implements JisuankeService {

    Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private OJService ojService;

    @Autowired
    private ContestRepository contestRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ContestRecordRepository contestRecordRepository;

    @Autowired
    private ContestProblemRecordRepository contestProblemRecordRepository;

    @Override
    public void addContest(String ojName, String cId) throws Exception {
        WebDriver webDriver = new ChromeDriver();
        webDriver.get("https://passport.jisuanke.com/?n=https://www.jisuanke.com/contest/" + cId);
        login(webDriver);
        if(webDriver.getTitle().contains("404")) {
            webDriver.close();
            throw new Exception("不存在的比赛");
        }
        Document document = Jsoup.parse(webDriver.getPageSource());
        webDriver.close();
        String title = document.selectFirst("h2").text();
        String contestInfo = document.selectFirst("div.contest_info").text();
        int problemNumber = Integer.parseInt(contestInfo.substring(contestInfo.indexOf("题目数量")).split("：")[1]);
        String html = restTemplate.getForObject("https://nanti.jisuanke.com/contest?kw=" + title, String.class);
        assert html != null;
        document = Jsoup.parse(html);
        Elements table = document.selectFirst("tbody").selectFirst("tr").select("td");
        String startTime = table.get(3).text() + ":00";
        String timeLength = table.get(4).text();
        log.info(startTime);
        log.info(timeLength);
        long len = (Integer.parseInt(timeLength.split(" ")[0]) * 60 + Integer.parseInt(timeLength.split(" ")[2])) * 60 * 1000;
        Contest contest = new Contest();
        contest.setTitle(title);
        contest.setOjName(ojName);
        contest.setCid(cId);
        contest.setStartTime(new Timestamp(Timestamp.valueOf(startTime).getTime() + 8*60*60*1000));
        contest.setEndTime(new Timestamp(contest.getStartTime().getTime() + len));
        contest.setProblemNumber(problemNumber);
        contestRepository.save(contest);
    }

    @Override
    @Transactional
    public void addContestRecord(String ojName, String cId, String studentId, Integer teamId, String account) throws Exception {
        Contest contest = contestRepository.findByOjNameAndCid(ojName, cId).get();

        if(System.currentTimeMillis() < contest.getEndTime().getTime()) {
            throw new Exception("比赛还没结束");
        }
        WebDriver webDriver = new ChromeDriver();
        webDriver.get("https://passport.jisuanke.com/?n=https://www.jisuanke.com/contest/" + cId + "?view=rank&page=1&school=%E5%8D%8E%E5%8D%97%E5%86%9C%E4%B8%9A%E5%A4%A7%E5%AD%A6#/");
        login(webDriver);
        Document document = Jsoup.parse(webDriver.getPageSource());
        webDriver.close();
        Elements table = document.selectFirst("tbody").select("tr");
        boolean hasTakePartIn = false;
        for (Element element : table) {
            Elements tds = element.select("td");
            if (tds.get(2).selectFirst("a").text().equals(account)) {
                hasTakePartIn = true;
                break;
            }
        }
        if(!hasTakePartIn) {
            throw new Exception("未参加该竞赛");
        }
        ContestRecord contestRecord = new ContestRecord();
        contestRecord.setTime(contest.getStartTime());
        contestRecord.setAccount(account);
        contestRecord.setTeamId(teamId);
        contestRecord.setStudentId(studentId);
        contestRecord.setContestId(contest.getId());
        contestRecordRepository.save(contestRecord);
        contestRecord = contestRecordRepository.findByContestIdAndStudentIdAndTeamId(contest.getId(), studentId, teamId).get();
        updateContestProblemRecord(contestRecord);
    }

    @Override
    @Transactional
    public void updateContestProblemRecord(ContestRecord contestRecord) throws Exception {
        Contest contest = contestRepository.findById(contestRecord.getContestId()).get();
        if(System.currentTimeMillis() < contest.getEndTime().getTime()) {
            return;
        }
        String cId = contest.getCid();
        WebDriver webDriver = new ChromeDriver();
        webDriver.get("https://passport.jisuanke.com/?n=https://www.jisuanke.com/contest/" + cId + "?view=rank&page=1&school=%E5%8D%8E%E5%8D%97%E5%86%9C%E4%B8%9A%E5%A4%A7%E5%AD%A6#/");
        login(webDriver);
        Document document = Jsoup.parse(webDriver.getPageSource());
        webDriver.close();



        Elements table = document.selectFirst("tbody").select("tr");
        for (Element element : table) {
            Elements tds = element.select("td");
            if (tds.get(2).selectFirst("a").text().equals(contestRecord.getAccount())) {

                char index = 'A';
                for (int i = 0; i < contest.getProblemNumber(); i++) {
                    String problemIndex = String.valueOf(index);

                    String statusString = tds.get(i+6).text();
                    int penalty = 0;
                    String status;

                    int tries = Integer.parseInt(statusString.substring(statusString.indexOf("(")+1, statusString.indexOf(")")));
                    if (statusString.contains("--")) {
                        status = "UnSolved";
                    } else {
                        penalty = Integer.parseInt(statusString.substring(0, statusString.indexOf("(")));
                        status = "Solved";
                    }

                    ContestProblemRecord contestProblemRecord = contestProblemRecordRepository.findByContestRecordIdAndProblemIndex(contestRecord.getId(), problemIndex).orElse(new ContestProblemRecord());
                    contestProblemRecord.setProblemIndex(problemIndex);
                    contestProblemRecord.setContestRecordId(contestRecord.getId());
                    contestProblemRecord.setStatus(status);
                    contestProblemRecord.setPenalty(penalty);
                    contestProblemRecord.setTries(tries);
                    contestProblemRecordRepository.save(contestProblemRecord);

                    ++index;
                }

                break;
            }
        }
    }

    @Override
    public void login(WebDriver webDriver) throws InterruptedException {
        WebElement username = webDriver.findElements(By.tagName("input")).get(0);
        username.sendKeys("15914764919");
        WebElement password = webDriver.findElements(By.tagName("input")).get(1);
        password.sendKeys("QqSt631665391");
        WebElement submit = webDriver.findElement(By.linkText("登录"));
        submit.click();
        Thread.sleep(2000);
    }
}
