package cn.edu.scau.acm.acmer.repository;

import cn.edu.scau.acm.acmer.entity.ScoreRecordView;
import cn.edu.scau.acm.acmer.model.QualifyingAcChart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ScoreRecordViewRepository extends JpaRepository<ScoreRecordView, Integer> {
    List<ScoreRecordView> findAllByQualifyingIdOrderByScoreAsc(Integer qualifyingId);

    @Query("select new cn.edu.scau.acm.acmer.model.QualifyingAcChart(scoreRecordView.students, sum(scoreRecordView.solvedNumber), sum(scoreRecordView.upSolvedNumber)) from ScoreRecordView as scoreRecordView left join Qualifying as qualifying on scoreRecordView.qualifyingId = qualifying.id left join Contest as contest on contest.id = qualifying.contestId where qualifying.seasonId = :seasonId and contest.ojName not in ('Base', 'CfRating') group by scoreRecordView.teamId, scoreRecordView.seasonStudentId, qualifying.seasonId")
    List<QualifyingAcChart> findAllAcChartBySeasonId(Integer seasonId);
}
