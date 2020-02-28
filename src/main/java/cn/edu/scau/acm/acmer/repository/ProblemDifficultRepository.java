package cn.edu.scau.acm.acmer.repository;

import cn.edu.scau.acm.acmer.entity.ProblemDifficult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface ProblemDifficultRepository extends JpaRepository<ProblemDifficult, Integer> {

    @Query(value = "select avg(problemDifficult.difficult) from ProblemDifficult as problemDifficult where problemDifficult.problemId = :problemId")
    BigDecimal avgByProblemId(@Param("problemId") Integer problemId);

    Optional<ProblemDifficult> findByProblemIdAndStudentId(Integer problemId, String studentId);
}
