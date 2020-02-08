package cn.edu.scau.acm.acmer.repository;

import cn.edu.scau.acm.acmer.entity.OjAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OjAccountRepository extends JpaRepository<OjAccount, Integer> {
    Optional<OjAccount> findByStudentIdAndOjName(String studentId, String ojName);
    List<OjAccount> findAllByOjName(String ojName);
    List<OjAccount> findAllByStudentId(String studentId);
}
