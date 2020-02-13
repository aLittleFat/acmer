package cn.edu.scau.acm.acmer.repository;

import cn.edu.scau.acm.acmer.entity.ContestProblem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContestProblemRepository extends JpaRepository<ContestProblem, Integer> {
    List<ContestProblem> findAllByContestIdOrderByIDinContest(int contestId);
    Optional<ContestProblem> findByContestIdAndIDinContest(Integer contestId, String idInContest);
}
