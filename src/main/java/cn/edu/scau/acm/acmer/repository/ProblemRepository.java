package cn.edu.scau.acm.acmer.repository;

import cn.edu.scau.acm.acmer.entity.Problem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProblemRepository extends JpaRepository<Problem, Integer> {
    Optional<Problem> findByOjNameAndProblemId(String ojName, String problemId);
}
