package cn.edu.scau.acm.acmer.service.impl;

import cn.edu.scau.acm.acmer.entity.Contest;
import cn.edu.scau.acm.acmer.entity.ContestRecord;
import cn.edu.scau.acm.acmer.repository.ContestRecordRepository;
import cn.edu.scau.acm.acmer.repository.ContestRepository;
import cn.edu.scau.acm.acmer.service.JisuankeService;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.util.*;

@Service
public class JisuankeServiceImpl implements JisuankeService {

    Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ContestRepository contestRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ContestRecordRepository contestRecordRepository;

    @Value("${scau.acmer.ojaccounts.jisuanke.username}")
    private String username;

    @Value("${scau.acmer.ojaccounts.jisuanke.password}")
    private String password;

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
        List<String> problemList = new ArrayList<>();
        for (char i = 'A', j = 0; j < problemNumber; j++, i++) {
            problemList.add(String.valueOf(i));
        }
        contest.setProblemList(StringUtils.join(problemList, " "));
        contestRepository.save(contest);
    }

    @Override
    @Transactional
    public void addContestRecord(String ojName, String cId, String studentId, Integer teamId, String account) throws Exception {
        Contest contest = contestRepository.findByOjNameAndCid(ojName, cId).get();
        WebDriver webDriver = new ChromeDriver();
        Elements table;
        webDriver.get("https://passport.jisuanke.com/?n=https://www.jisuanke.com/contest/" + cId + "?view=rank&page=1&school=%E5%8D%8E%E5%8D%97%E5%86%9C%E4%B8%9A%E5%A4%A7%E5%AD%A6#/");
        login(webDriver);
        while (true){
            Document document = Jsoup.parse(webDriver.getPageSource());
            Element thead = document.selectFirst("thead");
            Elements theadThs = thead.select("th");
            int accountIndex = 0;
            int problemIndexOffset = 0;
            int penaltyIndex = 0;
            for (int i = 0; i < theadThs.size(); i++) {
                Element theadTh = theadThs.get(i);
                if(theadTh.text().equals("用户名")) {
                    accountIndex = i;
                }
                if(theadTh.text().equals("A")) {
                    problemIndexOffset = i;
                }
                if(theadTh.text().equals("用时")) {
                    penaltyIndex = i;
                }
            }
            log.info(String.valueOf(accountIndex));
            log.info(String.valueOf(problemIndexOffset));
            table = document.selectFirst("tbody").select("tr");
//                log.info(table.toString());
            boolean hasTakePartIn = false;
            for (Element element : table) {
                Elements tds = element.select("td");
                log.info(tds.get(accountIndex).select("a").text());
                if (tds.get(accountIndex).selectFirst("a").text().equals(account)) {
                    ContestRecord contestRecord = new ContestRecord();
                    contestRecord.setTime(contest.getStartTime());
                    contestRecord.setAccount(account);
                    contestRecord.setTeamId(teamId);
                    contestRecord.setStudentId(studentId);
                    contestRecord.setContestId(contest.getId());
                    contestRecord.setPenalty(Integer.parseInt(tds.get(penaltyIndex).text()) * 60);

                    Set<String> solved = new TreeSet<>();

                    List<String> problemList = Arrays.asList(contest.getProblemList().split(" "));

                    for (int i = 0; i < problemList.size(); i++) {
                        String statusString = tds.get(i + problemIndexOffset).text();
                        if (!statusString.contains("--")) {
                            solved.add(problemList.get(i));
                        }
                    }
                    contestRecord.setSolved(StringUtils.join(solved, " "));
                    contestRecord.setUpSolved("");
                    contestRecordRepository.save(contestRecord);
                    hasTakePartIn = true;
                    break;
                }
            }
            try {
                webDriver.findElement(By.className("jsk-icon-angle-right")).click();
                Thread.sleep(2000);
            } catch (Exception e) {
                break;
            }
        }
        webDriver.close();
//        if(!hasTakePartIn) {
//            throw new Exception("未参加该竞赛");
//        }
    }

    @Override
    public void login(WebDriver webDriver) throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(webDriver, 10);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("input")));
        List<WebElement> webElements = webDriver.findElements(By.tagName("input"));
        for(WebElement webElement: webElements) {
            log.info(webElement.getAttribute("placeholder"));
            if(webElement.getAttribute("placeholder").contains("请输入你的邮箱或手机号")) {
                webElement.sendKeys(username);
            } else if (webElement.getAttribute("placeholder").contains("请输入你的密码")) {
                webElement.sendKeys(password);
                break;
            }
        }
        WebElement submit = webDriver.findElement(By.linkText("登录"));
        submit.click();
        Thread.sleep(5000);
    }
}
