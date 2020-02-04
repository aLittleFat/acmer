package cn.edu.scau.acm.acmer.repository;

import cn.edu.scau.acm.acmer.entity.Problem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProblemRepository extends JpaRepository<Problem, Integer> {
    Problem findByOjNameAndProblemId(String ojName, String problemId);
    Problem findById(int id);
}
