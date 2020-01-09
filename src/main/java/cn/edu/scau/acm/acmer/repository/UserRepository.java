package cn.edu.scau.acm.acmer.repository;

import cn.edu.scau.acm.acmer.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    User findByEmail(String Email);
}
