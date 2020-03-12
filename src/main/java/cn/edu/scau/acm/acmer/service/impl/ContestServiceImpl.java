package cn.edu.scau.acm.acmer.service.impl;

import cn.edu.scau.acm.acmer.entity.Contest;
import cn.edu.scau.acm.acmer.entity.ContestRecord;
import cn.edu.scau.acm.acmer.repository.ContestProblemRepository;
import cn.edu.scau.acm.acmer.repository.ContestRecordRepository;
import cn.edu.scau.acm.acmer.repository.ContestRepository;
import cn.edu.scau.acm.acmer.service.*;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class ContestServiceImpl implements ContestService {

    Logger log = LoggerFactory.getLogger(this.getClass());

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
    private NowCoderService nowCoderService;

    @Autowired
    private JisuankeService jisuankeService;

    @Autowired
    private CfService cfService;

    @Autowired
    private OJService ojService;


    @Override
    @Transactional
    public void addContestRecord(String ojName, String cId, String studentId, Integer teamId, String account, String password) throws Exception {

        ojService.addOj(ojName);
        addContest(ojName, cId, account, password);
        switch (ojName) {
            case "VJ": vjService.addContestRecord(ojName, cId, studentId, teamId, account, password); break;
            case "HDU": hduService.addContestRecord(ojName, cId, studentId, teamId, account, password); break;
            case "CodeForces": cfService.addContestRecord(ojName, cId, studentId, teamId, account);  break;
            case "计蒜客": jisuankeService.addContestRecord(ojName, cId, studentId, teamId, account);break;
            case "牛客": nowCoderService.addContestRecord(ojName, cId, studentId, teamId, account); break;
        }
    }

    @Override
    public void addContest(String ojName, String cId, String username, String password) throws Exception {
        Optional<Contest> optionalContest = contestRepository.findByOjNameAndCid(ojName, cId);
        if(optionalContest.isEmpty()) {
            switch (ojName) {
                case "VJ": vjService.addContest(ojName, cId, password); break;
                case "HDU": hduService.addContest(ojName, cId, username, password); break;
                case "CodeForces": cfService.addContest(ojName, cId); break;
                case "计蒜客": jisuankeService.addContest(ojName, cId);break;
                case "牛客": nowCoderService.addContest(ojName, cId); break;
                default: throw new Exception("不支持的OJ");
            }
        }
    }

    @Override
    public JSONObject getContestByStudentId(String studentId) {
        List<ContestRecord> contestRecords = contestRecordRepository.findAllByStudentIdOrderByTimeDesc(studentId);
        long a = System.currentTimeMillis();
        JSONObject res = getContestRecordTable(contestRecords);
        log.info(System.currentTimeMillis()-a + "ms");

        return res;
    }

    @Override
    public JSONObject getContestByTeamId(Integer teamId) {
        List<ContestRecord> contestRecords = contestRecordRepository.findAllByTeamIdOrderByTimeDesc(teamId);
        return getContestRecordTable(contestRecords);
    }

    private JSONObject getContestRecordTable(List<ContestRecord> contestRecords){
        JSONObject jsonObject = new JSONObject();
        Set<String> set = new TreeSet<>();
        List<JSONObject> contestLines = new ArrayList<>();

        for (ContestRecord contestRecord : contestRecords) {

            Contest contest = contestRepository.findById(contestRecord.getContestId()).get();
            if(contest.getEndTime().getTime() > System.currentTimeMillis()) continue;

            set.addAll(Arrays.asList(contest.getProblemList().split(" ")));

            JSONObject contestLine = new JSONObject();
            contestLine.put("contestId" ,contestRecord.getContestId());
            contestLine.put("title" ,contest.getTitle());
            contestLine.put("time" ,contestRecord.getTime());
            contestLine.put("proNum" ,contest.getProblemNumber());
            JSONObject cellClassName = new JSONObject();

            for (String problemIndex : contestRecord.getSolved().split(" ")) {
                cellClassName.put(problemIndex, "table-ac-cell");
                contestLine.put(problemIndex, "");
            }

            for (String problemIndex : contestRecord.getUpSolved().split(" ")) {
                cellClassName.put(problemIndex, "table-up-cell");
                contestLine.put(problemIndex, "");
            }

            contestLine.put("cellClassName", cellClassName);
            contestLine.put("solved", contestRecord.getSolved().split(" ").length);
            contestLine.put("penalty", contestRecord.getPenalty() / 60);
            contestLines.add(contestLine);

        }

        jsonObject.put("contests", contestLines);
        jsonObject.put("columns", set);

        return jsonObject;
    }
}
