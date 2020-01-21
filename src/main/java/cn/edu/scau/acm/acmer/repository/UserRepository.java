package cn.edu.scau.acm.acmer.repository;

import cn.edu.scau.acm.acmer.entity.User;
import cn.edu.scau.acm.acmer.model.User_Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByEmail(String Email);
    User findById(int id);

    @Query(value = "select new cn.edu.scau.acm.acmer.model.User_Student(user, student) from User as user left join Student as student on user.id = student.userId where user.verify = 0",
        countQuery = "select count(user) from User as user where user.verify = 0")
    Page<User_Student> findAllUnVerify(Pageable pageable);


}
