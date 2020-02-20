package cn.edu.scau.acm.acmer.repository;

import cn.edu.scau.acm.acmer.entity.ContestRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContestRecordRepository extends JpaRepository<ContestRecord, Integer> {
    Optional<ContestRecord> findByContestIdAndStudentIdAndTeamId(Integer contestId, String studentId, Integer teamId);
    List<ContestRecord> findAllByStudentIdOrderByTimeDesc(String studentId);
    List<ContestRecord> findAllByContestId(int id);
}
