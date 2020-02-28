package cn.edu.scau.acm.acmer.repository;

import cn.edu.scau.acm.acmer.entity.ProblemTag;
import cn.edu.scau.acm.acmer.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProblemTagRepository extends JpaRepository<ProblemTag, Integer> {
    Optional<ProblemTag> findByProblemIdAndStudentIdAndTagName(Integer problemId, String studentId, String tagName);


    @Query(value = "select distinct problemTag.tagName from ProblemTag as problemTag where problemTag.problemId = :problemId")
    List<String> findAllByProblemId(@Param("problemId") Integer problemId);

    @Query(value = "select distinct problemTag.tagName from ProblemTag as problemTag where problemTag.problemId = :problemId and problemTag.studentId = :studentId")
    List<String> findAllByProblemIdAndStudentId(@Param("problemId")Integer id, @Param("studentId")String studentId);
}
