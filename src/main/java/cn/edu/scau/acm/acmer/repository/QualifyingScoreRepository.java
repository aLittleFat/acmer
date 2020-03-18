package cn.edu.scau.acm.acmer.repository;

import cn.edu.scau.acm.acmer.entity.QualifyingScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QualifyingScoreRepository extends JpaRepository<QualifyingScore, Integer> {
    Optional<QualifyingScore> findByQualifyingIdAndSeasonStudentIdAndTeamId(Integer qualifyingId, Integer seasonStudentId, Integer teamId);
}
