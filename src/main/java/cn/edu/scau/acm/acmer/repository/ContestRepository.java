package cn.edu.scau.acm.acmer.repository;

import cn.edu.scau.acm.acmer.entity.Contest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContestRepository extends JpaRepository<Contest, Integer> {
}
