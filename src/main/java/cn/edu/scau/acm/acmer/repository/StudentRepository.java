package cn.edu.scau.acm.acmer.repository;

import cn.edu.scau.acm.acmer.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, String> {
    Student findByUserId(int Id);
}
