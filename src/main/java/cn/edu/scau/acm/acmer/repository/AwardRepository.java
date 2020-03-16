package cn.edu.scau.acm.acmer.repository;

import cn.edu.scau.acm.acmer.entity.Award;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AwardRepository extends JpaRepository<Award, Integer> {
    List<Award> findAllByTeamIdOrderByTimeAsc(Integer teamId);

    List<Award> findAllByTeamIdAndVerifiedOrderByTimeAsc(Integer teamId, Byte verified);
}
