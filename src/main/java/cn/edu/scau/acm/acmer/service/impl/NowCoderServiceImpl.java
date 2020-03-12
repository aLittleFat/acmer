package cn.edu.scau.acm.acmer.service.impl;

import cn.edu.scau.acm.acmer.entity.Contest;
import cn.edu.scau.acm.acmer.entity.ContestRecord;
import cn.edu.scau.acm.acmer.repository.ContestRecordRepository;
import cn.edu.scau.acm.acmer.repository.ContestRepository;
import cn.edu.scau.acm.acmer.service.NowCoderService;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.util.*;

@Service
public class NowCoderServiceImpl implements NowCoderService {

    Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ContestRecordRepository contestRecordRepository;

    @Autowired
    private ContestRepository contestRepository;


    @Override
    @Transactional
    public void addContestRecord(String ojName, String cId, String studentId, Integer teamId, String account) throws Exception {
        Contest contest = contestRepository.findByOjNameAndCid(ojName, cId).get();
        Optional<ContestRecord> optionalContestRecord = contestRecordRepository.findByContestIdAndStudentIdAndTeamId(contest.getId(), studentId, teamId);
        if(optionalContestRecord.isPresent()) {
            throw new Exception("已添加该竞赛记录");
        }

        String url = "https://ac.nowcoder.com/acm/contest/rank/submit-list?currentContestId=" + cId + "&contestList=" + cId;
        JSONObject res = restTemplate.getForObject(url, JSONObject.class);

        boolean hasTakePartIn = false;

        JSONArray signUpUsers = res.getJSONObject("data").getJSONArray("submitDataList").getJSONObject(0).getJSONArray("signUpUsers");
        for (int i = 0; i < signUpUsers.size(); ++i) {
            JSONObject signUpUser = signUpUsers.getJSONObject(i);
            if(String.valueOf(signUpUser.getInteger("uid")).equals(account)) {
                hasTakePartIn = true;
                break;
            }
        }

        if(!hasTakePartIn) {
            throw new Exception("没有参加该竞赛");
        }

        ContestRecord contestRecord = new ContestRecord();
        contestRecord.setContestId(contest.getId());
        contestRecord.setStudentId(studentId);
        contestRecord.setTeamId(teamId);
        contestRecord.setAccount(account);
        contestRecord.setTime(contest.getStartTime());

        int contestLength = (int) ((contest.getEndTime().getTime() - contest.getStartTime().getTime()) / 1000);
        int penalty = 0;

        JSONArray submissions = res.getJSONObject("data").getJSONArray("submitDataList").getJSONObject(0).getJSONArray("submissions");

        HashMap<Integer, String> map = new HashMap<>();
        HashMap<String, Integer> penaltyMap = new HashMap<>();

        JSONArray problemData = res.getJSONObject("data").getJSONArray("problemData");
        for(int i = 0; i < problemData.size(); ++i) {
            JSONObject problem = problemData.getJSONObject(i);
            map.put(problem.getInteger("problemId"), problem.getString("index"));
            penaltyMap.put(problem.getString("index"), 0);
        }

        Set<String> solved  = new TreeSet<>();
        Set<String> upSolved = new TreeSet<>();

        for (int i = 0; i < submissions.size(); ++i) {
            JSONObject submission = submissions.getJSONObject(i);
            if(String.valueOf(submission.getInteger("uid")).equals(account)) {


                String index = map.get(submission.getInteger("problemId"));
                int isAc = submission.getInteger("status");
                int time = (int) ((submission.getLong("submitTime") + 8*60*60*1000 - contest.getStartTime().getTime()) / 1000);
                log.info(index + " " + isAc + " " + time );
                if (isAc == 5) {
                    if (time > contestLength) {
                        if(!solved.contains(index)) {
                            upSolved.add(index);
                        }
                    } else {
                        if(!solved.contains(index)) {
                            solved.add(index);
                            penalty += penaltyMap.get(index) + time;
                        }
                    }
                } else if (isAc != 12) {
                    penaltyMap.put(index, penaltyMap.get(index) + 1200);
                }
            }
        }

        contestRecord.setSolved(StringUtils.join(solved, " "));
        contestRecord.setUpSolved(StringUtils.join(upSolved, " "));
        contestRecord.setPenalty(penalty);

        contestRecordRepository.save(contestRecord);
    }

    @Override
    public void addContest(String ojName, String cId) throws Exception {
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
            JSONArray problemData = res.getJSONObject("data").getJSONArray("problemData");
            contest.setProblemNumber(problemData.size());
            List<String> indexList = new ArrayList<>();
            for (int i = 0; i < problemData.size(); i++) {
                JSONObject problem = problemData.getJSONObject(i);
                String index = problem.getString("index");
                indexList.add(index);
            }
            contest.setProblemList(StringUtils.join(indexList, " "));
        }
        contestRepository.save(contest);
    }


    @Override
    public void updateContestProblem(Contest contest) {
        //todo
    }
}
