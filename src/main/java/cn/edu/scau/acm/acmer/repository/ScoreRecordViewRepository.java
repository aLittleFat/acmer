package cn.edu.scau.acm.acmer.repository;

import cn.edu.scau.acm.acmer.entity.ScoreRecordView;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScoreRecordViewRepository extends JpaRepository<ScoreRecordView, Integer> {
    List<ScoreRecordView> findAllByQualifyingIdOrderByScoreAsc(Integer qualifyingId);
}
