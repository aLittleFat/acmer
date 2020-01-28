package cn.edu.scau.acm.acmer.repository;

import cn.edu.scau.acm.acmer.entity.ProblemACRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProblemACRecordRepository extends JpaRepository<ProblemACRecord, Integer> {
    ProblemACRecord findProblemACRecordByOjAccountIdAndAndProblemId(int ojAccountId, int problemId);
}
