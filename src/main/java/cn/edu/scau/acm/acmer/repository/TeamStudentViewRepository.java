package cn.edu.scau.acm.acmer.repository;

import cn.edu.scau.acm.acmer.entity.TeamStudentView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamStudentViewRepository extends JpaRepository<TeamStudentView, Integer> {
}
