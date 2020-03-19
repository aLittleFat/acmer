package cn.edu.scau.acm.acmer.repository;

import cn.edu.scau.acm.acmer.entity.ContestRecordView;
import cn.edu.scau.acm.acmer.model.ContestRecordLine;
import cn.edu.scau.acm.acmer.model.MultiContestRecordLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContestRecordViewRepository extends JpaRepository<ContestRecordView, Integer> {
    List<ContestRecordView> findAllByContestId(Integer contestId);
    List<ContestRecordView> findAllByTeamId(Integer teamId);
    List<ContestRecordView> findAllByStudentId(String studentId);

    @Query("select new cn.edu.scau.acm.acmer.model.MultiContestRecordLine(contestRecordView) from ContestRecordView as contestRecordView where contestRecordView.studentId = :studentId and contestRecordView.ojName <> 'Base' and contestRecordView.ojName <> 'CfRating' order by contestRecordView.time desc")
    List<MultiContestRecordLine> findAllMultiContestRecordLineByStudentId(@Param("studentId") String studentId);

    @Query("select new cn.edu.scau.acm.acmer.model.MultiContestRecordLine(contestRecordView) from ContestRecordView as contestRecordView where contestRecordView.teamId = :teamId and contestRecordView.ojName <> 'Base' and contestRecordView.ojName <> 'CfRating' order by contestRecordView.time desc")
    List<MultiContestRecordLine> findAllMultiContestRecordLineByTeamId(@Param("teamId") Integer teamId);

    @Query("select new cn.edu.scau.acm.acmer.model.ContestRecordLine(contestRecordView) from ContestRecordView as contestRecordView where contestRecordView.contestId = :contestId")
    List<ContestRecordLine> findAllContestRecordLineByContestId(@Param("contestId") Integer contestId);

    Optional<ContestRecordView> findByContestIdAndStudentIdAndAccount(Integer contestId, String studentId, String account);

    Optional<ContestRecordView> findByContestIdAndTeamIdAndAccount(Integer contestId, Integer teamId, String account);
}
