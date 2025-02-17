package cn.edu.scau.acm.acmer.service.impl;

import cn.edu.scau.acm.acmer.entity.Contest;
import cn.edu.scau.acm.acmer.entity.ContestProblem;
import cn.edu.scau.acm.acmer.entity.ContestRecord;
import cn.edu.scau.acm.acmer.entity.OjAccount;
import cn.edu.scau.acm.acmer.repository.ContestProblemRepository;
import cn.edu.scau.acm.acmer.repository.ContestRecordRepository;
import cn.edu.scau.acm.acmer.repository.ContestRepository;
import cn.edu.scau.acm.acmer.repository.OjAccountRepository;
import cn.edu.scau.acm.acmer.service.CfService;
import cn.edu.scau.acm.acmer.service.ProblemService;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sun.source.tree.Tree;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Service
public class CfServiceImpl implements CfService {

    Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private OjAccountRepository ojAccountRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ProblemService problemService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private ContestRepository contestRepository;

    @Autowired
    private ContestRecordRepository contestRecordRepository;

    @Autowired
    private ContestProblemRepository contestProblemRepository;


    @Override
    @Async
    public void getAcProblemsByCfAccount(OjAccount cfAccount) {
        int sz = 0;
        int start = 1;
        int retry = 20;
        do {
            String url = "https://codeforces.com/api/user.status?handle=" + cfAccount.getAccount() + "&from=" + start + "&count=20";
            String response;
            try {
                response = restTemplate.getForObject(url, String.class);
                JSONObject jsonObject = JSONObject.parseObject(response);
                JSONArray cfProblems = jsonObject.getJSONArray("result");
                sz = cfProblems.size();
                for (Object cfProblem : cfProblems) {
                    JSONObject jsonProblem = (JSONObject) cfProblem;
                    String proId = jsonProblem.getJSONObject("problem").getInteger("contestId") + jsonProblem.getJSONObject("problem").getString("index");
                    String ojName="CodeForces";
                    if(jsonProblem.getJSONObject("problem").getInteger("contestId") >= 9999) {
                        ojName="Gym";
                    }
                    problemService.addProblem(ojName, proId, jsonProblem.getJSONObject("problem").getString("name"));
                    if(problemService.addProblemAcRecord(problemService.findProblem(ojName, proId), cfAccount, (jsonProblem.getLong("creationTimeSeconds") + 8*60*60) * 1000)){
                        break;
                    }
                }
                retry = 20;
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    Thread.sleep(2000);
                    retry--;
                    log.error("网络错误，准备重试，重试第{}次", 20-retry);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                continue;
            }
            start += 20;
        } while (sz >= 20 && retry > 0);
    }

    @Override
    @Async
    public void getAllAcProblems() {
        List<OjAccount> ojAccounts = ojAccountRepository.findAllByOjName("CodeForces");
        for(OjAccount ojAccount : ojAccounts) {
            getAcProblemsByCfAccount(ojAccount);
        }
    }

    @Override
    public boolean checkCfAccount(String username, String password) {
        String verifyCode = stringRedisTemplate.opsForValue().get(username + "_Verify");
        return verifyCode != null && verifyCode.equals(password);
    }

    @Override
    @Transactional
    public void addContestRecord(String ojName, String cId, String studentId, Integer teamId, String account) throws Exception {

        Contest contest = contestRepository.findByOjNameAndCid(ojName, cId).get();
        JSONObject standingRow = null;
        String url;
        JSONArray problems;
        if(studentId != null) {
            url = "https://codeforces.com/api/contest.standings?showUnofficial=true&contestId=" + cId + "&handles=" + account;
            JSONObject res = restTemplate.getForObject(url, JSONObject.class);
            if(!res.getString("status").equals("OK")) {
                throw new Exception(res.getString("comment"));
            }

            res = res.getJSONObject("result");
            problems = res.getJSONArray("problems");
            JSONArray rows = res.getJSONArray("rows");
            if(rows.size() == 0) throw new Exception("未参加该竞赛");
            standingRow = rows.getJSONObject(0);

        } else {
            final int sz = 200;
            int from = 1;

            JSONArray rows = new JSONArray();
            do {
                url = "https://codeforces.com/api/contest.standings?showUnofficial=true&contestId=" + cId + "&from=" + from + "&count=" + sz;
                log.info(url);
                from += sz;
                JSONObject res = restTemplate.getForObject(url, JSONObject.class);
                if(!res.getString("status").equals("OK")) {
                    throw new Exception(res.getString("comment"));
                }
                if(!res.getString("status").equals("OK")) {
                    throw new Exception(res.getString("comment"));
                }
                ContestRecord contestRecord = new ContestRecord();
                res = res.getJSONObject("result");
                problems = res.getJSONArray("problems");
                rows = res.getJSONArray("rows");
                for (int i = 0; i < rows.size(); i++) {
                    JSONObject row = rows.getJSONObject(i);
                    String teamName = row.getJSONObject("party").getString("teamName");
                    if(teamName!=null) {
                        log.info(teamName + "" + account);
                    }
                    if(teamName != null && teamName.equals(account)) {
                        standingRow = row;
                        break;
                    }
                }
            } while(rows.size() == sz && standingRow == null);
            if(standingRow == null) {
                throw new Exception("未参加该竞赛");
            }
        }

        ContestRecord contestRecord = new ContestRecord();
        contestRecord.setTime(new Timestamp((standingRow.getJSONObject("party").getLong("startTimeSeconds") + 8*60*60)*1000));
        contestRecord.setContestId(contest.getId());
        contestRecord.setStudentId(studentId);
        contestRecord.setTeamId(teamId);
        contestRecord.setAccount(account);

        JSONArray standings = standingRow.getJSONArray("problemResults");

        int penalty = 0;
        Set<String> solved = new TreeSet<>();

        for (int i = 0; i < problems.size(); i++) {
            String index = problems.getJSONObject(i).getString("index");

            Integer timeSeconds = standings.getJSONObject(i).getInteger("bestSubmissionTimeSeconds");
            if(timeSeconds != null) {
                penalty += 1200*standings.getJSONObject(i).getInteger("rejectedAttemptCount") + timeSeconds;
                solved.add(index);
            }
        }
        contestRecord.setPenalty(penalty);
        contestRecord.setSolved(StringUtils.join(solved ," "));
        contestRecord.setUpSolved("");
        contestRecordRepository.save(contestRecord);
    }

    @Override
    @Transactional
    public void addContest(String ojName, String cId) throws Exception {
        JSONObject res = restTemplate.getForObject("https://codeforces.com/api/contest.standings?from=1&count=1&contestId=" + cId, JSONObject.class);
        if(!res.getString("status").equals("OK")) {
            throw new Exception(res.getString("comment"));
        }

        Contest contest = new Contest();

        Set<String> problemList = new TreeSet<>();

        res = res.getJSONObject("result");

        JSONObject contestInfo = res.getJSONObject("contest");

        Long startTimeSeconds = contestInfo.getLong("startTimeSeconds");
        log.info(String.valueOf(startTimeSeconds));
        log.info(String.valueOf(new Timestamp((startTimeSeconds+8*60*60)*1000)));
        contest.setStartTime(new Timestamp((startTimeSeconds+8*60*60)*1000));
        contest.setEndTime(new Timestamp(contest.getStartTime().getTime() + contestInfo.getInteger("durationSeconds")*1000));
        contest.setTitle(contestInfo.getString("name"));
        contest.setOjName(ojName);
        contest.setCid(cId);
        JSONArray problems = res.getJSONArray("problems");

        for (int i = 0; i < problems.size(); i++) {
            problemList.add(problems.getJSONObject(i).getString("index"));
        }
        contest.setProblemList(StringUtils.join(problemList, " "));
        contestRepository.save(contest);
        contest = contestRepository.findByOjNameAndCid(ojName, cId).get();
        for (int i = 0; i < problems.size(); i++) {
            problemService.addProblem(ojName, cId + problems.getJSONObject(i).getString("index"),  problems.getJSONObject(i).getString("name"));
            ContestProblem contestProblem = new ContestProblem();
            contestProblem.setProblemIndex(problems.getJSONObject(i).getString("index"));
            contestProblem.setContestId(contest.getId());
            contestProblem.setProblemId(problemService.findProblem(ojName, cId + problems.getJSONObject(i).getString("index")).getId());
            contestProblemRepository.save(contestProblem);
        }
    }
}
