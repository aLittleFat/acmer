package cn.edu.scau.acm.acmer.repository;

import cn.edu.scau.acm.acmer.entity.TrainingRecordView;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TrainingRecordViewRepository extends JpaRepository<TrainingRecordView, Integer> {
    List<TrainingRecordView> findAllByTrainingId(int trainingId);
    Optional<TrainingRecordView> findByTrainingIdAndTrainingParticipantId(int trainingId, int trainingParticipantId);
}
