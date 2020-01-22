package cn.edu.scau.acm.acmer.repository;

import cn.edu.scau.acm.acmer.entity.OJ;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OJRepository extends JpaRepository<OJ, String> {
    OJ findByName(String name);
}
