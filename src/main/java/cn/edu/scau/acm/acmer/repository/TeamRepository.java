package cn.edu.scau.acm.acmer.repository;

import cn.edu.scau.acm.acmer.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamRepository extends JpaRepository<Team, Integer> {
    List<Team> findAllBySeasonIdOrderByRank(Integer seasonId);

    @Query("select team from Team as team left join TeamStudent as teamStudent on team.id = teamStudent.teamId where teamStudent.studentId = :studentId order by team.seasonId desc")
    List<Team> findAllByStudentId(@Param("studentId") String studentId);
}
