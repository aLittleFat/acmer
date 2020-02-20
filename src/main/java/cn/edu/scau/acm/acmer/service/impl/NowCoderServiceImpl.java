package cn.edu.scau.acm.acmer.service.impl;

import cn.edu.scau.acm.acmer.entity.Contest;
import cn.edu.scau.acm.acmer.entity.ContestProblemRecord;
import cn.edu.scau.acm.acmer.entity.ContestRecord;
import cn.edu.scau.acm.acmer.repository.ContestProblemRecordRepository;
import cn.edu.scau.acm.acmer.repository.ContestRecordRepository;
import cn.edu.scau.acm.acmer.repository.ContestRepository;
import cn.edu.scau.acm.acmer.service.NowCoderService;
import cn.edu.scau.acm.acmer.service.OJService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class NowCoderServiceImpl implements NowCoderService {

    Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ContestRecordRepository contestRecordRepository;

    @Autowired
    private ContestRepository contestRepository;

    @Autowired
    private OJService ojService;

    @Autowired
    private ContestProblemRecordRepository contestProblemRecordRepository;

    @Override
    @Transactional
    public void addContestRecord(String ojName, String cId, String studentId, Integer teamId, String account) throws Exception {
        Optional<Contest> optionalContest = contestRepository.findByOjNameAndCid(ojName, cId);
        Contest contest;
        if(optionalContest.isEmpty()) {
            addContest(ojName, cId);
            contest = contestRepository.findByOjNameAndCid(ojName, cId).get();
        } else {
            contest = optionalContest.get();
        }
        Optional<ContestRecord> optionalContestRecord = contestRecordRepository.findByContestIdAndStudentIdAndTeamId(contest.getId(), studentId, teamId);
        if(optionalContestRecord.isPresent()) {
            throw new Exception("已添加该竞赛记录");
        }

        String url = "https://ac.nowcoder.com/acm/contest/rank/submit-list?currentContestId=" + cId + "&contestList=" + cId;
        JSONObject res = restTemplate.getForObject(url, JSONObject.class);

        int uid = 0;

        JSONArray signUpUsers = res.getJSONObject("data").getJSONArray("submitDataList").getJSONObject(0).getJSONArray("signUpUsers");
        for (int i = 0; i < signUpUsers.size(); ++i) {
            JSONObject signUpUser = signUpUsers.getJSONObject(i);
            if(signUpUser.getString("name").equals(account)) {
                uid = signUpUser.getInteger("uid");
                break;
            }
        }

        if(uid == 0) {
            throw new Exception("没有参加该竞赛");
        }

        ContestRecord contestRecord = new ContestRecord();
        contestRecord.setContestId(contest.getId());
        contestRecord.setStudentId(studentId);
        contestRecord.setTeamId(teamId);
        contestRecord.setAccount(account);
        contestRecord.setTime(contest.getStartTime());
        contestRecordRepository.save(contestRecord);

        contestRecord = contestRecordRepository.findByContestIdAndStudentIdAndTeamId(contest.getId(), studentId, teamId).get();
        updateContestProblemRecord(contestRecord);
    }

    @Override
    public void addContest(String ojName, String cId) throws Exception {
        ojService.addOj(ojName);
        String url = "https://ac.nowcoder.com/acm/contest/rank/submit-list?currentContestId=" + cId + "&contestList=" + cId;
        JSONObject res = restTemplate.getForObject(url, JSONObject.class);
        assert res != null;
        if (res.getInteger("code") == 1) {
            throw new Exception(res.getString("msg"));
        }
        JSONObject contestInfo = res.getJSONObject("data").getJSONArray("submitDataList").getJSONObject(0).getJSONObject("basicInfo");
        Contest contest = new Contest();
        contest.setTitle(contestInfo.getString("name"));
        contest.setOjName(ojName);
        contest.setCid(cId);
        contest.setStartTime(new Timestamp(contestInfo.getLong("startTime") + 8*60*60*1000));
        contest.setEndTime(new Timestamp(contestInfo.getLong("endTime") + 8*60*60*1000));
        if(System.currentTimeMillis() < contest.getEndTime().getTime()) {
            contest.setProblemNumber(0);
        } else {
            contest.setProblemNumber(res.getJSONObject("data").getJSONArray("problemData").size());
        }
        contestRepository.save(contest);
    }

    @Override
    @Async
    public void updateContestProblemRecord(ContestRecord contestRecord) throws Exception {
        Contest contest = contestRepository.findById(contestRecord.getContestId()).get();
        String cId = contest.getCid();
        String url = "https://ac.nowcoder.com/acm/contest/rank/submit-list?currentContestId=" + cId + "&contestList=" + cId;
        JSONObject res = restTemplate.getForObject(url, JSONObject.class);

        int uid = 0;

        JSONArray signUpUsers = res.getJSONObject("data").getJSONArray("submitDataList").getJSONObject(0).getJSONArray("signUpUsers");
        for (int i = 0; i < signUpUsers.size(); ++i) {
            JSONObject signUpUser = signUpUsers.getJSONObject(i);
            if(signUpUser.getString("name").equals(contestRecord.getAccount())) {
                uid = signUpUser.getInteger("uid");
                break;
            }
        }

        List<ContestProblemRecord> contestProblemRecords = new ArrayList<>();
        if(contest.getProblemNumber() == 0) {
            updateContestProblem(contest);
        }

        HashMap<Integer, String> map = new HashMap<>();
        HashMap<String, Integer> charToInt = new HashMap<>();

        JSONArray problemData = res.getJSONObject("data").getJSONArray("problemData");
        for(int i = 0; i < problemData.size(); ++i) {
            JSONObject problem = problemData.getJSONObject(i);
            map.put(problem.getInteger("problemId"), problem.getString("index"));
        }

        char index = 'A';

        for (int i = 0; i < contest.getProblemNumber(); ++i) {
            String problemIndex = String.valueOf(index);
            charToInt.put(problemIndex, i);
            ContestProblemRecord contestProblemRecord = contestProblemRecordRepository.findByContestRecordIdAndProblemIndex(contestRecord.getId(), problemIndex).orElse(new ContestProblemRecord());
            contestProblemRecord.setStatus("UnSolved");
            contestProblemRecord.setProblemIndex(problemIndex);
            contestProblemRecord.setTries(0);
            contestProblemRecord.setContestRecordId(contestRecord.getId());
            contestProblemRecords.add(contestProblemRecord);
            index++;
        }

        int contestLength = (int) ((contest.getEndTime().getTime() - contest.getStartTime().getTime()) / (1000 * 60));
        log.info(String.valueOf(contestLength));

        JSONArray submissions = res.getJSONObject("data").getJSONArray("submitDataList").getJSONObject(0).getJSONArray("submissions");

        for (int i = 0; i < submissions.size(); ++i) {
            JSONObject submission = submissions.getJSONObject(i);
            if(submission.getInteger("uid") == uid) {
                int proNum = charToInt.get(map.get(submission.getInteger("problemId")));
                int isAc = submission.getInteger("status");
                log.info(submission.getLong("submitTime") + " " + (submission.getLong("submitTime") + 8*60*60*1000) + " " + contest.getStartTime().getTime());
                int time = (int) ((submission.getLong("submitTime") + 8*60*60*1000 - contest.getStartTime().getTime()) / (60 * 1000));
                if(!contestProblemRecords.get(proNum).getStatus().equals("UnSolved")) continue;
                contestProblemRecords.get(proNum).setTries(contestProblemRecords.get(proNum).getTries() + 1);
                if (isAc == 5) {
                    if (time > contestLength) {
                        contestProblemRecords.get(proNum).setStatus("UpSolved");
                    } else {
                        contestProblemRecords.get(proNum).setStatus("Solved");
                    }
                    contestProblemRecords.get(proNum).setPenalty(time);
                }
            }
        }
        contestProblemRecordRepository.saveAll(contestProblemRecords);
    }

    @Override
    public void updateContestProblem(Contest contest) {
        //todo
    }
}
