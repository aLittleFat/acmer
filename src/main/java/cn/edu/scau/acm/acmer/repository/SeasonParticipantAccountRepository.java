package cn.edu.scau.acm.acmer.repository;

import cn.edu.scau.acm.acmer.entity.SeasonParticipantAccount;
import cn.edu.scau.acm.acmer.model.TeamAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SeasonParticipantAccountRepository extends JpaRepository<SeasonParticipantAccount, Integer> {
    List<SeasonParticipantAccount> findAllBySeasonAccountId(Integer seasonAccountId);

    Optional<SeasonParticipantAccount> findBySeasonAccountIdAndTeamIdAndSeasonStudentId(int seasonAccountId, Integer teamId, Integer seasonStudentId);

    @Query("select new cn.edu.scau.acm.acmer.model.TeamAccount(seasonAccount.title, seasonParticipantAccount.handle, seasonParticipantAccount.account, seasonParticipantAccount.password) from SeasonParticipantAccount as seasonParticipantAccount left join SeasonAccount as seasonAccount on seasonParticipantAccount.seasonAccountId = seasonAccount.id where seasonParticipantAccount.teamId = :teamId")
    List<TeamAccount> findAllTeamAccountByTeamId(@Param("teamId") Integer teamId);
}
