package cn.edu.scau.acm.acmer.repository;

import cn.edu.scau.acm.acmer.entity.ProblemView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface ProblemViewRepository extends JpaRepository<ProblemView, Integer> {
    Page<ProblemView> findAllByOjNameLikeOrProblemIdLikeOrTitleLikeAndDifficultBetweenAndTagsLike(String ojName, String problemId, String title, BigDecimal difficult, BigDecimal difficult2, String tags, Pageable pageable);
    Page<ProblemView> findAllByOjNameLikeAndDifficultBetweenAndTagsLikeOrProblemIdLikeAndDifficultBetweenAndTagsLikeOrTitleLikeAndDifficultBetweenAndTagsLike(String ojName, BigDecimal difficult, BigDecimal difficult2, String tags, String problemId, BigDecimal difficult3, BigDecimal difficult4, String tags2, String title, BigDecimal difficult5, BigDecimal difficult6, String tags3, Pageable pageable);
}
