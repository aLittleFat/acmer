package cn.edu.scau.acm.acmer.repository;

import cn.edu.scau.acm.acmer.entity.PersonalContestRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonalContestRecordRepository extends JpaRepository<PersonalContestRecord, Integer> {
}
