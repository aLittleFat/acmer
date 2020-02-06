package cn.edu.scau.acm.acmer.repository;

import cn.edu.scau.acm.acmer.entity.ProblemACRecord;
import cn.edu.scau.acm.acmer.model.AcProblem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Repository
public interface ProblemACRecordRepository extends JpaRepository<ProblemACRecord, Integer> {
    ProblemACRecord findProblemACRecordByOjAccountIdAndProblemId(int ojAccountId, int problemId);
    List<ProblemACRecord> findAllByOjAccountIdAndTimeBetween(int ojAccountId, Date startTime, Date endTime);
    ProblemACRecord findFirstByOjAccountIdAndTimeBeforeOrderByTimeDesc(int ojAccountId, Date time);

    @Modifying
    @Transactional
    void deleteAllByOjAccountId(int ojAccountId);

    @Query("select new cn.edu.scau.acm.acmer.model.AcProblem(problem, problemAcRecord) from ProblemACRecord as problemAcRecord left join Problem as problem on problemAcRecord.problemId = problem.id where problemAcRecord.ojAccountId = :ojAccountId and problemAcRecord.time between :startTime and :endTime")
    List<AcProblem> findAcProblemByOjAccountIdAndTimeBetween(@Param("ojAccountId") int ojAccountId, @Param("startTime") Date startTime, @Param("endTime") Date endTime);

    @Query("select count (distinct problem) from ProblemACRecord as problemAcRecord left join Problem as problem on problemAcRecord.problemId=problem.id left join OJAccount as ojAccount on ojAccount.id=problemAcRecord.ojAccountId where ojAccount.studentId=:studentId")
    int countAllByStudnetId(@Param("studentId") String studentId);
}
