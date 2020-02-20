package cn.edu.scau.acm.acmer.repository;

import cn.edu.scau.acm.acmer.entity.TeamStudent;
import cn.edu.scau.acm.acmer.entity.TeamStudentPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeamStudentRepository extends JpaRepository<TeamStudent, TeamStudentPK> {
    Integer countAllByTeamId(Integer teamId);

    @Query("select count(teamStudent) from TeamStudent as teamStudent left join Team as team on teamStudent.teamId = team.id where teamStudent.studentId = :studentId and team.seasonId = :seasonId")
    Integer countByStudentIdAndSeasonId(@Param("studentId") String studentId, @Param("seasonId") Integer seasonId);
}
