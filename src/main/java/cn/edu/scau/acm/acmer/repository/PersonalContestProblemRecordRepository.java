package cn.edu.scau.acm.acmer.repository;

import cn.edu.scau.acm.acmer.entity.PersonalContestProblemRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonalContestProblemRecordRepository extends JpaRepository<PersonalContestProblemRecord, Integer> {
    List<PersonalContestProblemRecord> findAllByPersonalContestRecordId(Integer personalContestRecordId);
    Optional<PersonalContestProblemRecord> findByPersonalContestRecordIdAndContestProblemId(Integer personalContestRecordId, Integer contestProblemId);
}
