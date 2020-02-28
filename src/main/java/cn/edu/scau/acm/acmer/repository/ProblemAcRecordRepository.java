package cn.edu.scau.acm.acmer.repository;

import cn.edu.scau.acm.acmer.entity.ProblemAcRecord;
import cn.edu.scau.acm.acmer.model.AcProblem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProblemAcRecordRepository extends JpaRepository<ProblemAcRecord, Integer> {
    Optional<ProblemAcRecord> findProblemACRecordByOjAccountIdAndProblemId(int ojAccountId, int problemId);
    List<ProblemAcRecord> findAllByOjAccountIdAndTimeBetween(int ojAccountId, Date startTime, Date endTime);
    Optional<ProblemAcRecord> findFirstByOjAccountIdAndTimeBeforeOrderByTimeDesc(int ojAccountId, Date time);

    @Query(value = "select problemAcRecord from ProblemAcRecord as problemAcRecord where problemAcRecord.ojAccountId in :ojAccounts and problemAcRecord.time < :time and problemAcRecord.id not in (select problemAcRecord1.id from problemAcRecord as problemAcRecord1 where problemAcRecord1.ojAccountId in :exOjAccounts) order by problemAcRecord.time desc")
    List<ProblemAcRecord> findFirstByStudentIdAndTimeBeforeOrderByTimeDescAndStudentIdNotEquals(@Param("ojAccounts") List<Integer> ojAccounts, @Param("time") Date time, @Param("exOjAccounts") List<Integer> exOjAccounts);

    @Query(value = "select new cn.edu.scau.acm.acmer.model.AcProblem(problem, problemAcRecord) from ProblemAcRecord as problemAcRecord left join Problem as problem on problemAcRecord.problemId = problem.id where problemAcRecord.ojAccountId in :ojAccounts and problemAcRecord.time between :startTime and :endTime and problemAcRecord.ojAccountId not in :exOjAccounts")
    List<AcProblem> findAcProblemByOjAccountsAndTimeBetweenAndExceptByOjAccounts(@Param("ojAccounts") List<Integer> ojAccounts, @Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("exOjAccounts") List<Integer> exOjAccounts);

    @Modifying
    @Transactional
    void deleteAllByOjAccountId(int ojAccountId);

    @Query(value = "select new cn.edu.scau.acm.acmer.model.AcProblem(problem, problemAcRecord) from ProblemAcRecord as problemAcRecord left join Problem as problem on problemAcRecord.problemId = problem.id where problemAcRecord.ojAccountId = :ojAccountId and problemAcRecord.time between :startTime and :endTime")
    List<AcProblem> findAcProblemByOjAccountIdAndTimeBetween(@Param("ojAccountId") int ojAccountId, @Param("startTime") Date startTime, @Param("endTime") Date endTime);

    @Query(value = "select count (distinct problem) from ProblemAcRecord as problemAcRecord left join Problem as problem on problemAcRecord.problemId=problem.id left join OjAccount as ojAccount on ojAccount.id=problemAcRecord.ojAccountId where ojAccount.studentId=:studentId")
    int countAllByStudentId(@Param("studentId") String studentId);

    @Query(value = "select count (problemAcRecord) from ProblemAcRecord as problemAcRecord left join OjAccount as ojAccount on problemAcRecord.ojAccountId=ojAccount.id where problemAcRecord.problemId = :problemId and ojAccount.studentId = :studentId")
    int isAc(@Param("problemId") Integer problemId, @Param("studentId") String studentId);
}
