package cn.edu.scau.acm.acmer.service.impl;

import cn.edu.scau.acm.acmer.entity.Contest;
import cn.edu.scau.acm.acmer.entity.ContestRecord;
import cn.edu.scau.acm.acmer.httpclient.BaseHttpClient;
import cn.edu.scau.acm.acmer.repository.ContestRecordRepository;
import cn.edu.scau.acm.acmer.repository.ContestRepository;
import cn.edu.scau.acm.acmer.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ScheduledServiceImpl implements ScheduledService {

    @Autowired
    private BzojService bzojService;

    @Autowired
    private CfService cfService;

    @Autowired
    private HduService hduService;

    @Autowired
    private VjService vjService;

    @Autowired
    private NowCoderService nowCoderService;

    @Autowired
    private ContestRepository contestRepository;

    @Autowired
    private ContestService contestService;

    @Autowired
    private ContestRecordRepository contestRecordRepository;

    @Override
    @Async
    @Scheduled(cron = "0 0 2 * * ?")
    public void getAllAcProblemsFromOj() {
        bzojService.getAllAcProblems();
        cfService.getAllAcProblems();
        hduService.getAllAcProblems();
        vjService.getAllAcProblems();
    }

    @Override
    @Async
    @Scheduled(cron = "0 0 3 * * ?")
    public void updateAllContest() throws Exception {
        List<Contest> contests = contestRepository.findAllByOjName("VJ");
        BaseHttpClient vjClient = null;
        for (Contest contest : contests) {
            try {
                vjService.updateContest(vjClient, contest);
            }catch (Exception ignored){}
        }
        contests = contestRepository.findAllByOjName("牛客");
        for (Contest contest : contests) {
            try {
                nowCoderService.updateContest(contest);
            }catch (Exception ignored){}
        }
        List<String> ojNames = new ArrayList<>();
        ojNames.add("VJ");
        ojNames.add("HDU");
        ojNames.add("CodeForces");
        ojNames.add("Gym");
        contests = contestRepository.findAllByOjNameIn(ojNames);
        for (Contest contest : contests) {
            List<ContestRecord> contestRecords = contestRecordRepository.findAllByContestId(contest.getId());
            for (ContestRecord contestRecord : contestRecords) {
                try {
                    contestService.updateUpSolved(contestRecord);
                } catch (Exception ignored) {
                }
            }
        }
    }
}
