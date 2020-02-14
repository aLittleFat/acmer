package cn.edu.scau.acm.acmer.repository;

import cn.edu.scau.acm.acmer.entity.QualifyingScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QualifyingScoreRepository extends JpaRepository<QualifyingScore, Integer> {
}
