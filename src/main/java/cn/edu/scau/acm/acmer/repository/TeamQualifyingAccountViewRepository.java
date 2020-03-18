package cn.edu.scau.acm.acmer.repository;

import cn.edu.scau.acm.acmer.entity.TeamQualifyingAccountView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamQualifyingAccountViewRepository extends JpaRepository<TeamQualifyingAccountView, Integer> {
    List<TeamQualifyingAccountView> findAllByQualifyingId(Integer qualifyingId);
}
