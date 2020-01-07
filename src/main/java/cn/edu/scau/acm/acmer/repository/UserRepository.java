package cn.edu.scau.acm.acmer.repository;

import cn.edu.scau.acm.acmer.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User,Long> { }
