package cn.edu.scau.acm.acmer.repository;

import cn.edu.scau.acm.acmer.entity.SeasonStudent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SeasonStudentRepository extends JpaRepository<SeasonStudent, Integer> {
    Optional<SeasonStudent> findBySeasonIdAndStudentId(int seasonId, String studentId);
}
