package cn.edu.scau.acm.acmer.repository;

import cn.edu.scau.acm.acmer.entity.ContestProblem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContestProblemRepository extends JpaRepository<ContestProblem, Integer> {
    List<ContestProblem> findAllByContestIdOrderByIDinContest(int contestId);
}
