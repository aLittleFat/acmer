package cn.edu.scau.acm.acmer.repository;

import cn.edu.scau.acm.acmer.entity.OJAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OJAccountRepository extends JpaRepository<OJAccount, Integer> {
    OJAccount findByStudentIdAndOjName(String studentId, String ojName);
    List<OJAccount> findAllByOjName(String ojName);
}
