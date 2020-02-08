package cn.edu.scau.acm.acmer.repository;

import cn.edu.scau.acm.acmer.entity.PersonalContestProblemRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonalContestProblemRecordRepository extends JpaRepository<PersonalContestProblemRecord, Integer> {
}
