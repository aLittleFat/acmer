package cn.edu.scau.acm.acmer.service.impl;

import cn.edu.scau.acm.acmer.entity.*;
import cn.edu.scau.acm.acmer.repository.*;
import cn.edu.scau.acm.acmer.service.ContestService;
import cn.edu.scau.acm.acmer.service.QualifyingContestRecordService;
import cn.edu.scau.acm.acmer.service.QualifyingScoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QualifyingContestRecordServiceImpl implements QualifyingContestRecordService {

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
    private SeasonRepository seasonRepository;

    @Autowired
    private SeasonStudentRepository seasonStudentRepository;

    @Autowired
    private OjAccountRepository ojAccountRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private QualifyingScoreService qualifyingScoreService;

    @Override
    @Async
    public void updateQualifyingContestRecord(Integer qualifyingId) {
        Qualifying qualifying = qualifyingRepository.findById(qualifyingId).get();
        Season season = seasonRepository.findById(qualifying.getSeasonId()).get();
        if(season.getType().equals("个人赛")) {
            updatePersonalQualifyingContestRecord(qualifyingId);
        } else {
            updateTeamQualifyingContestRecord(qualifyingId);
        }
        qualifyingScoreService.calcScore(qualifyingId);
    }

    @Override
    public void updatePersonalQualifyingContestRecord(Integer qualifyingId) {
        Qualifying qualifying = qualifyingRepository.findById(qualifyingId).get();
        Contest contest = contestRepository.findById(qualifying.getContestId()).get();
        if(contest.getOjName().equals("VJ")) {
            List<SeasonStudent> seasonStudents = seasonStudentRepository.findAllBySeasonId(qualifying.getSeasonId());
            for (SeasonStudent seasonStudent : seasonStudents) {
                String studentId = seasonStudent.getStudentId();
                OjAccount ojAccount = ojAccountRepository.findByStudentIdAndOjName(studentId, "VJ").get();
                try {
                    contestService.addContestRecord(contest.getOjName(), contest.getCid(), studentId, null, ojAccount.getAccount(), null);
                } catch (Exception e) {
                    log.error(e.getMessage());
                }
            }
        } else {
            List<SeasonParticipantAccount> seasonParticipantAccounts = seasonParticipantAccountRepository.findAllBySeasonAccountId(qualifying.getSeasonAccountId());
            for (SeasonParticipantAccount seasonParticipantAccount : seasonParticipantAccounts) {
                SeasonStudent seasonStudent = seasonStudentRepository.findById(seasonParticipantAccount.getSeasonStudentId()).get();
                String studentId = seasonStudent.getStudentId();
                try {
                    contestService.addContestRecord(contest.getOjName(), contest.getCid(), studentId, null, seasonParticipantAccount.getHandle(), null);
                } catch (Exception e) {
                    log.error(e.getMessage());
                }
            }
        }
    }

    @Override
    public void updateTeamQualifyingContestRecord(Integer qualifyingId) {
        Qualifying qualifying = qualifyingRepository.findById(qualifyingId).get();
        Contest contest = contestRepository.findById(qualifying.getContestId()).get();
        if(contest.getOjName().equals("VJ")) {
            List<Team> teams = teamRepository.findAllBySeasonIdOrderByRank(qualifying.getSeasonId());
            for (Team team : teams) {
                try {
                    contestService.addContestRecord(contest.getOjName(), contest.getCid(), null, team.getId(), team.getVjAccount(), null);
                } catch (Exception e) {
                    log.error(e.getMessage());
                }
            }
        } else {
            List<SeasonParticipantAccount> seasonParticipantAccounts = seasonParticipantAccountRepository.findAllBySeasonAccountId(qualifying.getSeasonAccountId());
            for (SeasonParticipantAccount seasonParticipantAccount : seasonParticipantAccounts) {
                try {
                    contestService.addContestRecord(contest.getOjName(), contest.getCid(), null, seasonParticipantAccount.getTeamId(), seasonParticipantAccount.getHandle(), null);
                } catch (Exception e) {
                    log.error(e.getMessage());
                }
            }
        }
    }
}
