package cn.edu.scau.acm.acmer.service.impl;

import cn.edu.scau.acm.acmer.entity.*;
import cn.edu.scau.acm.acmer.model.CfRating;
import cn.edu.scau.acm.acmer.model.OjAcChart;
import cn.edu.scau.acm.acmer.model.QualifyingAcChart;
import cn.edu.scau.acm.acmer.repository.*;
import cn.edu.scau.acm.acmer.service.*;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.util.*;

import static java.lang.Integer.max;

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

    @Autowired
    private ScoreRecordViewRepository scoreRecordViewRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private ContestRecordRepository contestRecordRepository;

    @Autowired
    private QualifyingScoreService qualifyingScoreService;

    @Autowired
    private SeasonStudentRepository seasonStudentRepository;

    @Autowired
    private OjAccountRepository ojAccountRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private OJService ojService;

    @Override
    public List<Qualifying> getBySeasonId(Integer seasonId) {
        return qualifyingRepository.findAllBySeasonId(seasonId);
    }

    @Override
    public List<Qualifying> getBySeasonIdCalculated(Integer seasonId) {
        Season season = seasonRepository.findById(seasonId).get();
        List<Qualifying> qualifyings = qualifyingRepository.findAllBySeasonIdAndCalculated(seasonId, (byte)1);
        boolean hasCf = false;
        for (Qualifying qualifying : qualifyings) {
            if (qualifying.getTitle().equals("CF积分")) {
                hasCf = true;
                break;
            }
        }
        if(hasCf) {
            double sumProportion = 0;
            for (Qualifying qualifying : qualifyings) {
                sumProportion += qualifying.getProportion();
            }
            for (Qualifying qualifying : qualifyings) {
                if (qualifying.getTitle().equals("CF积分")) {
                    qualifying.setProportion(sumProportion / (1 - season.getCfProportion()) * season.getCfProportion());
                }
            }
        }
        return qualifyings;
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
        qualifyingContestRecordService.updateQualifyingContestRecord(qualifying.getId());
    }

    @Override
    @Transactional
    public void addBaseQualifying(Integer seasonId) throws Exception {
        Season season = seasonRepository.findById(seasonId).get();
        if(!season.getType().equals("组队赛")) {
            throw new Exception("不是组队赛");
        }
        List<Team> teams = teamRepository.findAllBySeasonIdOrderByRankNumAsc(seasonId);
        int sz = teams.size();
        List<String> problemList = new ArrayList<>();
        char ch = 'A';
        for (int i = 0; i < sz; i++) {
            problemList.add(String.valueOf(ch));
            ++ch;
        }
        ojService.addOj("Base");
        Contest contest;
        Optional<Contest> optionalContest = contestRepository.findByOjNameAndCid("Base", String.valueOf(seasonId));
        if(optionalContest.isEmpty()) {
            contest = new Contest();
            contest.setOjName("Base");
            contest.setCid(String.valueOf(seasonId));
            contest.setProblemList(StringUtils.join(problemList, " "));
            contest.setTitle("基础分");
            contest.setStartTime(new Timestamp(0));
            contest.setEndTime(new Timestamp(0));
        } else {
            contest = optionalContest.get();
            contest.setProblemList(StringUtils.join(problemList, " "));
        }
        contest = contestRepository.save(contest);
        List<String> solved = new ArrayList<>();
        ch = 'A';
        for (int i = sz-1; i >= 0; --i) {
            solved.add(String.valueOf(ch));
            ch++;
            ContestRecord contestRecord = contestRecordRepository.findByContestIdAndStudentIdAndTeamId(contest.getId(), null, teams.get(i).getId()).orElse(new ContestRecord());
            contestRecord.setTeamId(teams.get(i).getId());
            contestRecord.setContestId(contest.getId());
            contestRecord.setTime(contest.getStartTime());
            contestRecord.setAccount("");
            contestRecord.setPenalty(0);
            contestRecord.setUpSolved("");
            contestRecord.setSolved(StringUtils.join(solved, " "));
            contestRecordRepository.save(contestRecord);
        }
        Qualifying qualifying = new Qualifying();
        qualifying.setCalculated((byte)0);
        qualifying.setContestId(contest.getId());
        qualifying.setTitle("基础分");
        qualifying.setSeasonId(seasonId);
        qualifying.setProportion(2);
        qualifying = qualifyingRepository.save(qualifying);
        qualifyingScoreService.calcScore(qualifying.getId());
    }

    @Override
    public void addCfQualifying(Integer seasonId) throws Exception {
        Season season = seasonRepository.findById(seasonId).get();
        if(!season.getType().equals("个人赛")) {
            throw new Exception("不是个人赛");
        }
        List<SeasonStudent> seasonStudents = seasonStudentRepository.findAllBySeasonId(seasonId);
        List<String> problemList = new ArrayList<>();
        char ch = 'A';
        for (int i = 0; i < 10; i++) {
            problemList.add(String.valueOf(ch));
            ++ch;
        }
        Contest contest;
        ojService.addOj("CfRating");
        Optional<Contest> optionalContest = contestRepository.findByOjNameAndCid("CfRating", String.valueOf(seasonId));
        if(optionalContest.isEmpty()) {
            contest = new Contest();
            contest.setOjName("CfRating");
            contest.setCid(String.valueOf(seasonId));
            contest.setProblemList(StringUtils.join(problemList, " "));
            contest.setTitle("CF积分");
            contest.setStartTime(new Timestamp(0));
            contest.setEndTime(new Timestamp(0));
        } else {
            contest = optionalContest.get();
            contest.setProblemList(StringUtils.join(problemList, " "));
        }
        contest = contestRepository.save(contest);

        List<CfRating> cfRatings = new ArrayList<>();

        for (SeasonStudent seasonStudent : seasonStudents) {
            Optional<OjAccount> optionalOjAccount = ojAccountRepository.findByStudentIdAndOjName(seasonStudent.getStudentId(), "CodeForces");
            CfRating cfRating = new CfRating();
            cfRating.setStudentId(seasonStudent.getStudentId());
            if(optionalOjAccount.isEmpty()) {
                cfRating.setSolved(0);
                cfRating.setCfRating(0);
            } else {
                String cfHandle = optionalOjAccount.get().getAccount();
                JSONObject jsonObject = restTemplate.getForObject("https://codeforces.com/api/user.rating?handle=" + cfHandle, JSONObject.class);
                if (jsonObject.getString("status").equals("OK")) {
                    Integer rating = 0;
                    int count = 0;
                    Long time = System.currentTimeMillis() / 1000 - 3*30*24*60*60;
                    JSONArray ratingList = jsonObject.getJSONArray("result");
                    List<Integer> ratings = new ArrayList<>();
                    for (int j = 0; j < ratingList.size(); j++) {
                        JSONObject ratingData = ratingList.getJSONObject(j);
                        if(ratingData.getLong("ratingUpdateTimeSeconds") >= time) {
                            ratings.add(ratingData.getInteger("newRating"));
                            count++;
                        }
                    }
                    ratings.sort(Integer::compareTo);
                    for (int i = ratings.size()-1; i >= max(0, ratings.size()-3) ; i--) {
                        rating += ratings.get(i);
                    }
                    rating /= 3;
                    if(count < 5) {
                        rating = rating * 9 / 10;
                    }
                    cfRating.setCfRating(rating);
                    if (rating == 0) {
                        cfRating.setSolved(0);
                    } else if (rating < 1200) {
                        cfRating.setSolved(1);
                    } else if (rating < 1400) {
                        cfRating.setSolved(2);
                    } else if (rating < 1600) {
                        cfRating.setSolved(3);
                    } else if (rating < 1900) {
                        cfRating.setSolved(4);
                    } else if (rating < 2100) {
                        cfRating.setSolved(5);
                    } else if (rating < 2300) {
                        cfRating.setSolved(6);
                    } else if (rating < 2400) {
                        cfRating.setSolved(7);
                    } else if (rating < 2600) {
                        cfRating.setSolved(8);
                    } else if (rating < 3000) {
                        cfRating.setSolved(9);
                    } else {
                        cfRating.setSolved(10);
                    }
                } else {
                    cfRating.setSolved(0);
                    cfRating.setCfRating(0);
                }
            }
            cfRatings.add(cfRating);
        }

        cfRatings.sort((o1,o2) -> o2.getCfRating().compareTo(o1.getCfRating()));



        List<String> solved = new ArrayList<>();
        ch = 'A';
        for (int i = cfRatings.size() - 1; i >= 0; --i) {
            while (solved.size() < cfRatings.get(i).getSolved()) {
                solved.add(String.valueOf(ch));
                ch++;
            }
            ContestRecord contestRecord = contestRecordRepository.findByContestIdAndStudentIdAndTeamId(contest.getId(), cfRatings.get(i).getStudentId(), null).orElse(new ContestRecord());
            contestRecord.setStudentId(cfRatings.get(i).getStudentId());
            contestRecord.setContestId(contest.getId());
            contestRecord.setTime(contest.getStartTime());
            contestRecord.setAccount("");
            if (solved.size() == 0) {
                contestRecord.setPenalty(0);
            } else {
                contestRecord.setPenalty(i*60);
            }
//            log.info(i + " " + cfRatings.get(i).getCfRating() + " " + contestRecord.getPenalty());
            contestRecord.setUpSolved("");
            contestRecord.setSolved(StringUtils.join(solved, " "));
            contestRecordRepository.save(contestRecord);
        }
        Qualifying qualifying = new Qualifying();
        qualifying.setCalculated((byte)0);
        qualifying.setContestId(contest.getId());
        qualifying.setTitle("CF积分");
        qualifying.setSeasonId(seasonId);
        qualifying.setProportion(0);
        qualifying = qualifyingRepository.save(qualifying);
        qualifyingScoreService.calcScore(qualifying.getId());
    }

    @Override
    public void deleteQualifying(Integer qualifyingId) throws Exception {
        Optional<Qualifying> optionalQualifying = qualifyingRepository.findById(qualifyingId);
        if(optionalQualifying.isEmpty()) {
            throw new Exception("不存在的排位赛");
        }
        qualifyingRepository.delete(optionalQualifying.get());
    }

    @Override
    public List<ScoreRecordView> getQualifyingScoreByQualifyingId(Integer qualifyingId) {
        return scoreRecordViewRepository.findAllByQualifyingIdOrderByScoreAsc(qualifyingId);
    }

    @Override
    public Contest getContestByQualifyingId(Integer qualifyingId) {
        Qualifying qualifying = qualifyingRepository.findById(qualifyingId).get();
        return contestRepository.findById(qualifying.getContestId()).get();
    }

    @Override
    public List<QualifyingAcChart> getQualifyingAcChartBySeasonId(Integer seasonId) {
        List<QualifyingAcChart> qualifyingAcCharts = scoreRecordViewRepository.findAllAcChartBySeasonId(seasonId);
        Collections.sort(qualifyingAcCharts);
        return qualifyingAcCharts;
    }

    @Override
    public void updateSeasonQualifying(Integer seasonId) {
        List<Qualifying> qualifyings = qualifyingRepository.findAllBySeasonId(seasonId);
        for (Qualifying qualifying: qualifyings) {
            qualifyingContestRecordService.updateQualifyingContestRecord(qualifying.getId());
        }
    }


}
