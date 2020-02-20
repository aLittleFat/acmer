package cn.edu.scau.acm.acmer.repository;

import cn.edu.scau.acm.acmer.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamRepository extends JpaRepository<Team, Integer> {
    List<Team> findAllBySeasonIdOrderByRank(Integer seasonId);
}
