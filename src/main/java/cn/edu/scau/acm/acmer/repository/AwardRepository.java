package cn.edu.scau.acm.acmer.repository;

import cn.edu.scau.acm.acmer.entity.Award;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AwardRepository extends JpaRepository<Award, Integer> {
    List<Award> findAllByTeamIdOrderByTimeAsc(Integer teamId);

    List<Award> findAllByTeamIdAndVerifiedOrderByTimeAsc(Integer teamId, Byte verified);

    @Query("select award from Award as award left join TeamStudent as teamStudent on award.teamId = teamStudent.teamId where teamStudent.studentId = :studentId and award.verified = 1 order by award.time asc")
    List<Award> findAllByStudentId(@Param("studentId") String studentId);
}
