package cn.edu.scau.acm.acmer.repository;

import cn.edu.scau.acm.acmer.entity.PersonalRankingScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonalRankingScoreRepository extends JpaRepository<PersonalRankingScore, Integer> {
}
