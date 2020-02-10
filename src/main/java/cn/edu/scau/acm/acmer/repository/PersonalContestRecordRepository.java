package cn.edu.scau.acm.acmer.repository;

import cn.edu.scau.acm.acmer.entity.PersonalContestRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonalContestRecordRepository extends JpaRepository<PersonalContestRecord, Integer> {
    Optional<PersonalContestRecord> findByContestIdAndStudentId(Integer contestId, String studentId);
    List<PersonalContestRecord> findAllByStudentId(String studentId);
}
