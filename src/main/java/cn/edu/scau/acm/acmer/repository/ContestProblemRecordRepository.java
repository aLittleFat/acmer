package cn.edu.scau.acm.acmer.repository;

import cn.edu.scau.acm.acmer.entity.ContestProblemRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContestProblemRecordRepository extends JpaRepository<ContestProblemRecord, Integer> {
    List<ContestProblemRecord> findAllByContestRecordId(Integer personalContestRecordId);
    Optional<ContestProblemRecord> findByContestRecordIdAndProblemIndex(Integer contestRecordId, String problemIndex);
}
