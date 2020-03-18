package cn.edu.scau.acm.acmer.service.impl;

import cn.edu.scau.acm.acmer.entity.*;
import cn.edu.scau.acm.acmer.repository.*;
import cn.edu.scau.acm.acmer.service.ContestService;
import cn.edu.scau.acm.acmer.service.QualifyingContestRecordService;
import cn.edu.scau.acm.acmer.service.QualifyingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class QualifyingServiceImpl implements QualifyingService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private QualifyingRepository qualifyingRepository;

    @Autowired
    private ContestService contestService;

    @Autowired
    private SeasonParticipantAccountRepository seasonParticipantAccountRepository;

    @Autowired
    private ContestRepository contestRepository;

    @Autowired
    private QualifyingContestRecordService qualifyingContestRecordService;

    @Autowired
    private SeasonRepository seasonRepository;

    @Override
    public List<Qualifying> getBySeasonId(Integer seasonId) {
        return qualifyingRepository.findAllBySeasonId(seasonId);
    }

    @Override
    public void addQualifying(Integer seasonId, String title, String ojName, String cId, String password, Double proportion, Integer seasonAccountId) throws Exception {
        try {
            contestService.addContest(ojName, cId, null, password);
        } catch (Exception e) {
            if (e.getMessage().equals("该比赛需要登录")) {
                List<SeasonParticipantAccount> seasonParticipantAccounts = seasonParticipantAccountRepository.findAllBySeasonAccountId(seasonAccountId);
                boolean hasAddContest = false;
                for (SeasonParticipantAccount seasonParticipantAccount: seasonParticipantAccounts) {
                    try {
                        contestService.addContest(ojName, cId, seasonParticipantAccount.getAccount(), seasonParticipantAccount.getPassword());
                        hasAddContest = true;
                    } catch (Exception ignored) { }
                    if (hasAddContest) break;
                }
                if (!hasAddContest) {
                    throw new Exception("账号集中没有正确的账号密码");
                }
            } else {
                throw e;
            }
        }
        Contest contest = contestRepository.findByOjNameAndCid(ojName, cId).get();
        Qualifying qualifying = new Qualifying();
        qualifying.setSeasonId(seasonId);
        qualifying.setTitle(title);
        qualifying.setContestId(contest.getId());
        qualifying.setSeasonAccountId(seasonAccountId);
        qualifying.setProportion(proportion);
        qualifying = qualifyingRepository.save(qualifying);
        if (contest.getEndTime().getTime() < System.currentTimeMillis()) {
            qualifyingContestRecordService.updateQualifyingContestRecord(qualifying.getId());
        }
    }


}
