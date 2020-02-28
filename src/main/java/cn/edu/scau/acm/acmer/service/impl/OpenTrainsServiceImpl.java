package cn.edu.scau.acm.acmer.service.impl;

import cn.edu.scau.acm.acmer.entity.Contest;
import cn.edu.scau.acm.acmer.entity.ContestRecord;
import cn.edu.scau.acm.acmer.httpclient.BaseHttpClient;
import cn.edu.scau.acm.acmer.repository.ContestProblemRecordRepository;
import cn.edu.scau.acm.acmer.repository.ContestRecordRepository;
import cn.edu.scau.acm.acmer.repository.ContestRepository;
import cn.edu.scau.acm.acmer.service.OJService;
import cn.edu.scau.acm.acmer.service.OpenTrainsService;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
public class OpenTrainsServiceImpl implements OpenTrainsService {

    Logger log = LoggerFactory.getLogger(this.getClass());

    @Value("${scau.acmer.ojaccounts.opentrains.password}")
    private String password;

    @Autowired
    private ContestRepository contestRepository;

    @Autowired
    private ContestRecordRepository contestRecordRepository;

    @Autowired
    private ContestProblemRecordRepository contestProblemRecordRepository;

    @Autowired
    private OJService ojService;

    @Override
    public String login(String username) throws Exception {
        BaseHttpClient httpClient = new BaseHttpClient();
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("uname", username));
        params.add(new BasicNameValuePair("psw", password));
        params.add(new BasicNameValuePair("Enter", "Enter"));
        String res = httpClient.post("http://opentrains.snarknews.info/~ejudge/index.cgi", params);
        if(res.contains("Login unsuccessful")) {
            throw new Exception("登录失败，用户名或密码错误");
        }
        String href = Jsoup.parse(res).selectFirst("a").attr("href");
        String sid = href.substring(href.indexOf("sid=")+4);
        log.info(sid);
        return sid;
    }

    @Override
    public void addContest(String ojName, String cId, String username) throws Exception {
        if (contestRepository.findByOjNameAndCid(ojName, cId).isPresent()) return;
        ojService.addOj(ojName);
        Contest contest = new Contest();
        contest.setOjName(ojName);
        contest.setCid(cId);
        String sid = login(username);
        BaseHttpClient httpClient = new BaseHttpClient();
        String info = httpClient.get("http://opentrains.snarknews.info/~ejudge/sn_sh.cgi?data=nonpriv_preview&sid=" + sid + "&contest=" + cId);
        if(info.contains("No log file")) {
            throw new Exception("比赛不存在");
        }
        Document document = Jsoup.parse(info);
        contest.setTitle(document.selectFirst("h2").text());

        info = httpClient.get("http://opentrains.snarknews.info/~ejudge/index.cgi?config=" + cId + "&sid=" + sid);

        int count = 0;
        int index = 0;
        while ((index = info.indexOf(info, index)) != -1) {
            index = index + info.length();
            count++;
        }

        contest.setProblemNumber(count);

        contest.setStartTime(new Timestamp(0));
        contest.setEndTime(new Timestamp(0));

        contestRepository.save(contest);
    }

    @Override
    public void addContestRecord(String ojName, String cId, String studentId, Integer teamId, String account) throws Exception {
        addContest(ojName, cId, account);
        Contest contest = contestRepository.findByOjNameAndCid(ojName, cId).get();
        String sid = login(account);
        BaseHttpClient httpClient = new BaseHttpClient();
        String res = httpClient.get("http://opentrains.snarknews.info/~ejudge/sn_sh.cgi?data=result_team&sid=" + sid + "&contest=" + cId);
        Element tr = Jsoup.parse(res).selectFirst("tr.solver");
        if(tr == null) {
            throw new Exception("没有参加该竞赛");
        }
        log.info(tr.text());
    }

    @Override
    public void updateContestProblemRecord(ContestRecord contestRecord) throws Exception {

    }
}
