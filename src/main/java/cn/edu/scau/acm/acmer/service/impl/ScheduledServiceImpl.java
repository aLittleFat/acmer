package cn.edu.scau.acm.acmer.service.impl;

import cn.edu.scau.acm.acmer.httpclient.BaseHttpClient;
import cn.edu.scau.acm.acmer.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

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
    @Scheduled(cron = "0 52 12 * * ?")
    public void updateAllContest() throws Exception {
        BaseHttpClient httpClient = null;
        vjService.updateAllContest(httpClient);
    }
}
