package cn.edu.scau.acm.acmer.repository;

import cn.edu.scau.acm.acmer.entity.Contest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContestRepository extends JpaRepository<Contest, Integer> {
    Optional<Contest> findByOjNameAndOjid(String ojName, String ojId);
}
