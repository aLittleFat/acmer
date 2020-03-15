package cn.edu.scau.acm.acmer.service.impl;

import cn.edu.scau.acm.acmer.entity.Contest;
import cn.edu.scau.acm.acmer.entity.ContestRecord;
import cn.edu.scau.acm.acmer.model.ContestRecordLine;
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
        List<ContestRecordLine> contestRecordLines = contestRecordRepository.findAllContestRecordLineByStudentId(studentId);
        return getContestRecordTable(contestRecordLines);
    }

    @Override
    public JSONObject getContestByTeamId(Integer teamId) {
        List<ContestRecordLine> contestRecordLines = contestRecordRepository.findAllContestRecordLineByTeamId(teamId);
        return getContestRecordTable(contestRecordLines);
    }

    private JSONObject getContestRecordTable(List<ContestRecordLine> contestRecordLines){
        JSONObject res = new JSONObject();
        Set<String> problemList = new TreeSet<>();
        for(ContestRecordLine contestRecordLine : contestRecordLines) {
            problemList.addAll(contestRecordLine.getProblemList());
        }
        res.put("problemList", problemList);
        res.put("contestRecord", contestRecordLines);
        return res;
    }
}
