package cn.edu.scau.acm.acmer.repository;

import cn.edu.scau.acm.acmer.entity.TrainingParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrainingParticipantRepository extends JpaRepository<TrainingParticipant, Integer> {
    List<TrainingParticipant> findALlByTrainingId(Integer trainingId);
    Optional<TrainingParticipant> findByTrainingIdAndStudentIdAndTeamId(Integer trainingId, String studentId, Integer teamId);
}
