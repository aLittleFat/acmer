package cn.edu.scau.acm.acmer.service.impl;

import cn.edu.scau.acm.acmer.entity.*;
import cn.edu.scau.acm.acmer.model.QualifyingContestRecord;
import cn.edu.scau.acm.acmer.model.Scores;
import cn.edu.scau.acm.acmer.model.SeasonParticipant;
import cn.edu.scau.acm.acmer.repository.*;
import cn.edu.scau.acm.acmer.service.QualifyingScoreService;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class QualifyingScoreServiceImpl implements QualifyingScoreService {

    @Autowired
    private QualifyingRepository qualifyingRepository;

    @Autowired
    private SeasonRepository seasonRepository;

    @Autowired
    private PersonalQualifyingAccountViewRepository personalQualifyingAccountViewRepository;

    @Autowired
    private TeamQualifyingAccountViewRepository teamQualifyingAccountViewRepository;

    @Autowired
    private ContestRecordViewRepository contestRecordViewRepository;

    @Autowired
    private QualifyingScoreRepository qualifyingScoreRepository;

    @Override
    public void calcScore(Integer qualifyingId) {
        Qualifying qualifying = qualifyingRepository.findById(qualifyingId).get();
        Season season = seasonRepository.findById(qualifying.getSeasonId()).get();
        List<QualifyingContestRecord> qualifyingContestRecords = new ArrayList<>();
        if(season.getType().equals("个人赛")) {
            List<PersonalQualifyingAccountView> personalQualifyingAccountViews = personalQualifyingAccountViewRepository.findAllByQualifyingId(qualifyingId);
            for (PersonalQualifyingAccountView personalQualifyingAccountView : personalQualifyingAccountViews) {
                Optional<ContestRecordView> optionalContestRecordView = contestRecordViewRepository.findByContestIdAndStudentIdAndAccount(qualifying.getContestId(), personalQualifyingAccountView.getStudentId(), personalQualifyingAccountView.getAccount());
                QualifyingContestRecord qualifyingContestRecord = new QualifyingContestRecord();
                qualifyingContestRecord.setSeasonStudentId(personalQualifyingAccountView.getSeasonStudentId());
                if(optionalContestRecordView.isEmpty()) {
                    qualifyingContestRecord.setPenalty(0);
                    qualifyingContestRecord.setSolved((long)0);
                } else {
                    qualifyingContestRecord.setPenalty(optionalContestRecordView.get().getPenalty());
                    qualifyingContestRecord.setSolved( optionalContestRecordView.get().getSolvedNumber());
                    qualifyingContestRecord.setContestRecordId(optionalContestRecordView.get().getId());
                }
                qualifyingContestRecords.add(qualifyingContestRecord);
            }
        } else {
            List<TeamQualifyingAccountView> teamQualifyingAccountViews = teamQualifyingAccountViewRepository.findAllByQualifyingId(qualifyingId);
            for (TeamQualifyingAccountView teamQualifyingAccountView : teamQualifyingAccountViews) {
                Optional<ContestRecordView> optionalContestRecordView = contestRecordViewRepository.findByContestIdAndTeamIdAndAccount(qualifying.getContestId() ,teamQualifyingAccountView.getTeamId(), teamQualifyingAccountView.getAccount());
                QualifyingContestRecord qualifyingContestRecord = new QualifyingContestRecord();
                qualifyingContestRecord.setTeamId(teamQualifyingAccountView.getTeamId());
                if(optionalContestRecordView.isEmpty()) {
                    qualifyingContestRecord.setPenalty(0);
                    qualifyingContestRecord.setSolved((long)0);
                } else {
                    qualifyingContestRecord.setPenalty(optionalContestRecordView.get().getPenalty());
                    qualifyingContestRecord.setSolved( optionalContestRecordView.get().getSolvedNumber());
                    qualifyingContestRecord.setContestRecordId(optionalContestRecordView.get().getId());
                }
                qualifyingContestRecords.add(qualifyingContestRecord);
            }
        }

        qualifyingContestRecords.sort((o1, o2) -> {
            if (o1.getSolved().equals(o2.getSolved())) {
                return o1.getPenalty() - o2.getPenalty();
            } else
                return (int) (o2.getSolved() - o1.getSolved());
        });

		int N = qualifyingContestRecords.size(), n;
		int[] a = new int[N + 1];
		for (n = 0; n < N; ++n) a[n] = Math.toIntExact(qualifyingContestRecords.get(n).getSolved()); //题数
		while (n != 0 && a[n - 1] == 0) --n;
		a[n++] = 0;

		double[] d = new double[N + 1], e = new double[N + 1];
		for (int i = 0; i < n; ++i)
			e[i] = d[i] = Math.sqrt(i + 1.0) - 1;
		int j = 0;
		for(int i = 0; i < n; ++i){
	        if (i + 1 < n && a[i] == a[i + 1]) {
	            d[i + 1] += d[i];
	        }
	        else{
	            int cnt = i - j + 1;
	            double even = d[i] / cnt;
	            double left  = 0.0;
	            for(int t = j; t <= i; ++t){
	                left = (even - e[t]) / 2;
	                e[t] += (even - e[t]) / 2;
	            }
	            left /= cnt;
	            for (int t = j; t <= i; ++t) e[t] += left;
	            j = i + 1;
			}
		}
		for (int i = 0; i < N; ++i)
            qualifyingContestRecords.get(i).setScore(e[Math.min(i, n - 1)]);

		for (QualifyingContestRecord qualifyingContestRecord : qualifyingContestRecords) {
		    Optional<QualifyingScore> optionalQualifyingScore = qualifyingScoreRepository.findByQualifyingIdAndSeasonStudentIdAndTeamId(qualifyingId, qualifyingContestRecord.getSeasonStudentId(), qualifyingContestRecord.getTeamId());
		    QualifyingScore qualifyingScore;
		    if(optionalQualifyingScore.isEmpty()) {
		        qualifyingScore = new QualifyingScore();
                qualifyingScore.setSeasonStudentId(qualifyingContestRecord.getSeasonStudentId());
                qualifyingScore.setTeamId(qualifyingContestRecord.getTeamId());
                qualifyingScore.setQualifyingId(qualifyingId);
		    } else {
		        qualifyingScore = optionalQualifyingScore.get();
            }
            qualifyingScore.setContestRecordId(qualifyingContestRecord.getContestRecordId());
		    qualifyingScore.setScore(qualifyingContestRecord.getScore());
		    qualifyingScoreRepository.save(qualifyingScore);
        }
		qualifying.setCalculated((byte) 1);
		qualifyingRepository.save(qualifying);
    }

    @Override
    public List<Scores> getSumScore(Integer seasonId) throws Exception {
        Optional<Season> optionalSeason = seasonRepository.findById(seasonId);
        if(optionalSeason.isEmpty()) {
            throw new Exception("不存在的赛季");
        }
        Season season = optionalSeason.get();
        List<SeasonParticipant> seasonParticipants;
        if(season.getType().equals("个人赛")) {
            seasonParticipants = seasonRepository.findAllSeasonStudentParticipantBySeasonId(seasonId);
        } else {
            seasonParticipants = seasonRepository.findAllSeasonTeamParticipantBySeasonId(seasonId);
        }
        List<Scores> scoresList = new ArrayList<>();
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
        for (SeasonParticipant seasonParticipant : seasonParticipants) {
            Scores scores = new Scores();
            scores.setParticipant(seasonParticipant);
            boolean allCal = true;
            List<Double> scoreList = new ArrayList<>();
            double sum = 0.0;
            for (Qualifying qualifying : qualifyings) {
                Optional<QualifyingScore> qualifyingScore = qualifyingScoreRepository.findByQualifyingIdAndSeasonStudentIdAndTeamId(qualifying.getId(), seasonParticipant.getSeasonStudentId(), seasonParticipant.getTeamId());
                if(qualifyingScore.isEmpty()) {
                    allCal = false;
                    break;
                } else {
                    scoreList.add(qualifyingScore.get().getScore());
                    sum += qualifyingScore.get().getScore() * qualifying.getProportion();
                }
            }
            if (allCal) {
                scores.setScoreList(scoreList);
                scores.setSumScore(sum);
                scoresList.add(scores);
            }
        }
        scoresList.sort(Comparator.comparing(Scores::getSumScore));
        return scoresList;

    }
}
