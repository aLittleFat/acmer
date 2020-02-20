package cn.edu.scau.acm.acmer.service.impl;

import cn.edu.scau.acm.acmer.entity.Contest;
import cn.edu.scau.acm.acmer.entity.ContestProblemRecord;
import cn.edu.scau.acm.acmer.entity.ContestRecord;
import cn.edu.scau.acm.acmer.httpclient.BaseHttpClient;
import cn.edu.scau.acm.acmer.repository.ContestProblemRecordRepository;
import cn.edu.scau.acm.acmer.repository.ContestProblemRepository;
import cn.edu.scau.acm.acmer.repository.ContestRecordRepository;
import cn.edu.scau.acm.acmer.repository.ContestRepository;
import cn.edu.scau.acm.acmer.service.ContestService;
import cn.edu.scau.acm.acmer.service.HduService;
import cn.edu.scau.acm.acmer.service.VjService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ContestServiceImpl implements ContestService {

    @Autowired
    private ContestRepository contestRepository;

    @Autowired
    private VjService vjService;

    @Autowired
    private HduService hduService;

    @Autowired
    private ContestProblemRepository contestProblemRepository;

    @Autowired
    private ContestRecordRepository contestRecordRepository;

    @Autowired
    private ContestProblemRecordRepository contestProblemRecordRepository;


    @Override
    public void addPersonalContestRecord(String ojName, String cId, String password, String studentId, String account) throws Exception {
        BaseHttpClient httpClient = null;
        int contestId = addContest(httpClient, ojName, cId, account, password);
        Optional<ContestRecord> contestRecord = contestRecordRepository.findByContestIdAndStudentId(contestId, studentId);
        if(contestRecord.isPresent()) {
            throw new Exception("已经添加该竞赛");
        }
        switch (ojName) {
            case "VJ": vjService.addPersonalContestRecord(httpClient, contestId, studentId, account); break;
            case "HDU": hduService.addPersonalContestRecord(httpClient, contestId, studentId, account); break;
            case "CodeForces": break;
            case "计蒜客": break;
            case "牛客": break;
        }
    }

    @Override
    public int addContest(BaseHttpClient httpClient, String ojName, String cId, String username, String password) throws Exception {
        Optional<Contest> contest = contestRepository.findByOjNameAndCid(ojName, cId);

        if(contest.isPresent()) return contest.get().getId();
        switch (ojName) {
            case "VJ": vjService.addContest(httpClient, cId, password); break;
            case "HDU": hduService.addContest(httpClient, cId, username, password); break;
            case "CodeForces": break;
            case "计蒜客": break;
            case "牛客": break;
            default: throw new Exception("不存在的OJ名");
        }
        return contestRepository.findByOjNameAndCid(ojName, cId).get().getId();
    }

    @Override
    public List<JSONObject> getContestByStudentId(String studentId) {
        List<JSONObject> contestLines = new ArrayList<>();
        List<ContestRecord> personalContestRecords = contestRecordRepository.findAllByStudentIdOrderByTimeDesc(studentId);
        for (ContestRecord personalContestRecord : personalContestRecords) {
            Contest contest = contestRepository.findById(personalContestRecord.getContestId()).get();
            if(contest.getEndTime().getTime() > System.currentTimeMillis()) continue;
            int solved = 0;
            int penalty = 0;
            JSONObject contestLine = new JSONObject();
            contestLine.put("contestId" ,personalContestRecord.getContestId());
            contestLine.put("title" ,contest.getTitle());
            contestLine.put("time" ,personalContestRecord.getTime());
            contestLine.put("proNum" ,contest.getProblemNumber());
            JSONObject cellClassName = new JSONObject();
            List<ContestProblemRecord> contestProblemRecords = contestProblemRecordRepository.findAllByContestRecordId(personalContestRecord.getId());
            char ch = 'A';
            for(ContestProblemRecord contestProblemRecord : contestProblemRecords) {
//                String problemIndex =  contestProblemRepository.findById(contestProblemRecord.getContestProblemId()).get().getProblemIndex();
                String problemIndex = String.valueOf(ch);
                ch++;
                switch (contestProblemRecord.getStatus()) {
                    case "Solved":
                        solved++;
                        penalty += contestProblemRecord.getPenalty();
                        contestLine.put(problemIndex, contestProblemRecord.getPenalty() + "(" +contestProblemRecord.getTries()  + ")");
                        cellClassName.put(problemIndex, "table-ac-cell");
                        break;
                    case "UpSolved":
                        contestLine.put(problemIndex, contestProblemRecord.getPenalty() + "(" +contestProblemRecord.getTries()  + ")");
                        cellClassName.put(problemIndex, "table-up-cell");
                        break;
                    case "UnSolved":
                        if(contestProblemRecord.getTries() > 0) {
                            contestLine.put(problemIndex, "(" +contestProblemRecord.getTries()  + ")");
                            cellClassName.put(problemIndex, "table-wa-cell");
                        }
                        else {
                            contestLine.put(problemIndex, "");
                        }
                        break;
                }
            }
            contestLine.put("cellClassName", cellClassName);
            contestLine.put("solved" ,solved);
            contestLine.put("penalty" ,penalty);
            contestLines.add(contestLine);
        }
        return contestLines;
    }
}
