package cn.edu.scau.acm.acmer.repository;

import cn.edu.scau.acm.acmer.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    List<User> findAllByStudentIdNotNullAndStatusNot(String status);

    List<User> findAllByGradeAndStatusNot(Integer grade, String status);

    Optional<User> findByEmail(String Email);

    Page<User> findAllByVerifiedEquals(Pageable pageable, byte verified);

    @Query(value = "select user from User as user where user.studentId in (select seasonStudent.studentId from SeasonStudent as seasonStudent where seasonStudent.seasonId = :seasonId)")
    List<User> findAllBySeasonId(@Param("seasonId") int seasonId);

    @Query(value = "select user from User as user where user.status = '现役' and user.studentId is not null and user.studentId not in (select seasonStudent.studentId from SeasonStudent as seasonStudent where seasonStudent.seasonId = :seasonId) order by user.grade asc ")
    List<User> findAllNotInSeasonBySeasonId(@Param("seasonId") int seasonId);

    @Query(value = "select user from User as user where user.status = '现役' and user.studentId is not null and user.studentId not in (select teamStudent.studentId from TeamStudent as teamStudent left join Team as team on teamStudent.teamId = team.id where team.seasonId = :seasonId) order by user.grade asc ")
    List<User> findAllNotInTeamBySeasonId(@Param("seasonId") int seasonId);

    Optional<User> findByStudentId(String studentId);

    @Query(value = "select user from User as user left join TeamStudent as teamStudent on user.studentId = teamStudent.studentId where teamStudent.teamId = :teamId")
    List<User> findAllByTeamId(@Param("teamId") Integer teamId);

    Page<User> findAllByStatus(Pageable pageable, String status);
}
