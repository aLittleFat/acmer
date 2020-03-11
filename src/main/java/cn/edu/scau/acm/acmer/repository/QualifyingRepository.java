package cn.edu.scau.acm.acmer.repository;

import cn.edu.scau.acm.acmer.entity.Qualifying;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QualifyingRepository extends JpaRepository<Qualifying, Integer> {
    List<Qualifying> findAllBySeasonId(Integer seasonId);
}
