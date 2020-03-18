package cn.edu.scau.acm.acmer.repository;

import cn.edu.scau.acm.acmer.entity.SeasonAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeasonAccountRepository extends JpaRepository<SeasonAccount, Integer> {
    List<SeasonAccount> findAllBySeasonId(Integer seasonId);
}
