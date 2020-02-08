package cn.edu.scau.acm.acmer.repository;

import cn.edu.scau.acm.acmer.entity.Oj;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OjRepository extends JpaRepository<Oj, String> {
    Optional<Oj> findByName(String name);
}
