package cn.edu.scau.acm.acmer.repository;

import cn.edu.scau.acm.acmer.entity.Season;
import cn.edu.scau.acm.acmer.model.SeasonParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeasonRepository extends JpaRepository<Season, Integer> {

    @Query("select new cn.edu.scau.acm.acmer.model.SeasonParticipant(team.id, 0, teamStudentView.students) from TeamStudentView as teamStudentView left join Team as team on teamStudentView.id = team.id where team.seasonId = :seasonId")
    List<SeasonParticipant> findAllSeasonTeamParticipantBySeasonId(Integer seasonId);

    @Query("select new cn.edu.scau.acm.acmer.model.SeasonParticipant(0, seasonStudent.id, user.name) from SeasonStudent as seasonStudent left join User as user on seasonStudent.studentId = user.studentId where seasonStudent.seasonId = :seasonId")
    List<SeasonParticipant> findAllSeasonStudentParticipantBySeasonId(Integer seasonId);
}
