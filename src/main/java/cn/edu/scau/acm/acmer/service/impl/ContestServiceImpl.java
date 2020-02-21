package cn.edu.scau.acm.acmer.service.impl;

import cn.edu.scau.acm.acmer.entity.Contest;
import cn.edu.scau.acm.acmer.entity.ContestProblemRecord;
import cn.edu.scau.acm.acmer.entity.ContestRecord;
import cn.edu.scau.acm.acmer.repository.ContestProblemRecordRepository;
import cn.edu.scau.acm.acmer.repository.ContestProblemRepository;
import cn.edu.scau.acm.acmer.repository.ContestRecordRepository;
import cn.edu.scau.acm.acmer.repository.ContestRepository;
import cn.edu.scau.acm.acmer.service.*;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

    @Autowired
    private NowCoderService nowCoderService;

    @Autowired
    private JisuankeService jisuankeService;


    @Override
    public void addContestRecord(String ojName, String cId, String studentId, Integer teamId, String account, String password) throws Exception {

        switch (ojName) {
            case "VJ": vjService.addContestRecord(ojName, cId, studentId, teamId, account, password); break;
            case "HDU": hduService.addContestRecord(ojName, cId, studentId, teamId, account, password); break;
            case "CodeForces": break;
            case "计蒜客": jisuankeService.addContestRecord(ojName, cId, studentId, teamId, account);break;
            case "牛客": nowCoderService.addContestRecord(ojName, cId, studentId, teamId, account); break;
        }
    }

//    @Override
//    public int addContest(BaseHttpClient httpClient, String ojName, String cId, String username, String password) throws Exception {
//        Optional<Contest> contest = contestRepository.findByOjNameAndCid(ojName, cId);
//
//        if(contest.isPresent()) return contest.get().getId();
//        switch (ojName) {
//            case "VJ": vjService.addContest(httpClient, cId, password); break;
//            case "HDU": hduService.addContest(httpClient, cId, username, password); break;
//            case "CodeForces": break;
//            case "计蒜客": break;
//            case "牛客": break;
//            default: throw new Exception("不存在的OJ名");
//        }
//        return contestRepository.findByOjNameAndCid(ojName, cId).get().getId();
//    }

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
            for(ContestProblemRecord contestProblemRecord : contestProblemRecords) {
                String problemIndex = contestProblemRecord.getProblemIndex();
                switch (contestProblemRecord.getStatus()) {
                    case "Solved":
                        solved++;
                        penalty += contestProblemRecord.getPenalty() + (contestProblemRecord.getTries() - 1)*20;
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
