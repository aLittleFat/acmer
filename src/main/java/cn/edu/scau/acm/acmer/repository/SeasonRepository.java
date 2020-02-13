package cn.edu.scau.acm.acmer.repository;

import cn.edu.scau.acm.acmer.entity.Season;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeasonRepository extends JpaRepository<Season, Integer> {
}
