package cn.edu.scau.acm.acmer.repository;

import cn.edu.scau.acm.acmer.entity.TrainingParticipantView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TrainingParticipantViewRepository extends JpaRepository<TrainingParticipantView, Integer> {
    List<TrainingParticipantView> findAllByTrainingId(Integer trainingId);
}
