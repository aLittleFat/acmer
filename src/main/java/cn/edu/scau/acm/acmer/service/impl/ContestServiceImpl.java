package cn.edu.scau.acm.acmer.service.impl;

import cn.edu.scau.acm.acmer.entity.Contest;
import cn.edu.scau.acm.acmer.entity.PersonalContestProblemRecord;
import cn.edu.scau.acm.acmer.entity.PersonalContestRecord;
import cn.edu.scau.acm.acmer.httpclient.BaseHttpClient;
import cn.edu.scau.acm.acmer.model.PersonalContestLine;
import cn.edu.scau.acm.acmer.repository.ContestProblemRepository;
import cn.edu.scau.acm.acmer.repository.ContestRepository;
import cn.edu.scau.acm.acmer.repository.PersonalContestProblemRecordRepository;
import cn.edu.scau.acm.acmer.repository.PersonalContestRecordRepository;
import cn.edu.scau.acm.acmer.service.ContestService;
import cn.edu.scau.acm.acmer.service.HduService;
import cn.edu.scau.acm.acmer.service.VjService;
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
    private PersonalContestRecordRepository personalContestRecordRepository;

    @Autowired
    private PersonalContestProblemRecordRepository personalContestProblemRecordRepository;


    @Override
    public void addPersonalContestRecord(String ojName, String cId, String password, String studentId, String account) throws Exception {
        BaseHttpClient httpClient = null;
        int contestId = addContest(httpClient, ojName, cId, account, password);
        Optional<PersonalContestRecord> personalContestRecord = personalContestRecordRepository.findByContestIdAndStudentId(contestId, studentId);
        if(personalContestRecord.isPresent()) {
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
        Optional<Contest> contest = contestRepository.findByOjNameAndCId(ojName, cId);

        if(contest.isPresent()) return contest.get().getId();
        switch (ojName) {
            case "VJ": vjService.addContest(httpClient, cId, password); break;
            case "HDU": hduService.addContest(httpClient, cId, username, password); break;
            case "CodeForces": break;
            case "计蒜客": break;
            case "牛客": break;
            default: throw new Exception("不存在的OJ名");
        }
        return contestRepository.findByOjNameAndCId(ojName, cId).get().getId();
    }

    @Override
    public List<PersonalContestLine> getPersonalContestByStudentId(String studentId) {
        List<PersonalContestLine> personalContestLines = new ArrayList<>();
        List<PersonalContestRecord> personalContestRecords = personalContestRecordRepository.findAllByStudentIdOrderByTimeDesc(studentId);
        for (PersonalContestRecord personalContestRecord : personalContestRecords) {
            Contest contest = contestRepository.findById(personalContestRecord.getContestId()).get();
            if(contest.getEndTime().getTime() > System.currentTimeMillis()) continue;
            int solved = 0;
            int penalty = 0;
            PersonalContestLine personalContestLine = new PersonalContestLine();
            personalContestLine.setContestId(personalContestRecord.getContestId());
            personalContestLine.setTitle(contest.getName());
            personalContestLine.setTime(personalContestRecord.getTime());
            personalContestLine.setProNum(contest.getProblemNumber());
            List<PersonalContestProblemRecord> personalContestProblemRecords = personalContestProblemRecordRepository.findAllByPersonalContestRecordId(personalContestRecord.getId());
            for(PersonalContestProblemRecord personalContestProblemRecord : personalContestProblemRecords) {
                if(personalContestProblemRecord.getStatus().equals("Solved")) {
                    solved++;
                    penalty += personalContestProblemRecord.getAcTime();
                }
            }
            personalContestLine.setPersonalContestProblemRecords(personalContestProblemRecords);
            personalContestLine.setSolved(solved);
            personalContestLine.setPenalty(penalty);
            personalContestLines.add(personalContestLine);
        }
        return personalContestLines;
    }
}
