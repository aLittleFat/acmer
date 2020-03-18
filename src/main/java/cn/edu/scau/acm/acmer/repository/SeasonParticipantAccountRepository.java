package cn.edu.scau.acm.acmer.repository;

import cn.edu.scau.acm.acmer.entity.SeasonParticipantAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeasonParticipantAccountRepository extends JpaRepository<SeasonParticipantAccount, Integer> {
    List<SeasonParticipantAccount> findAllBySeasonAccountId(Integer seasonAccountId);
}
