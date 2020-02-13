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

    @Modifying
    @Transactional
    void deleteAllByOjAccountId(int ojAccountId);

    @Query(value = "select new cn.edu.scau.acm.acmer.model.AcProblem(problem, problemAcRecord) from ProblemAcRecord as problemAcRecord left join Problem as problem on problemAcRecord.problemId = problem.id where problemAcRecord.ojAccountId = :ojAccountId and problemAcRecord.time between :startTime and :endTime")
    List<AcProblem> findAcProblemByOjAccountIdAndTimeBetween(@Param("ojAccountId") int ojAccountId, @Param("startTime") Date startTime, @Param("endTime") Date endTime);

    @Query(value = "select count (distinct problem) from ProblemAcRecord as problemAcRecord left join Problem as problem on problemAcRecord.problemId=problem.id left join OjAccount as ojAccount on ojAccount.id=problemAcRecord.ojAccountId where ojAccount.studentId=:studentId")
    int countAllByStudentId(@Param("studentId") String studentId);
}
