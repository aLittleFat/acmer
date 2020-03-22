package cn.edu.scau.acm.acmer.repository;

import cn.edu.scau.acm.acmer.entity.Training;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TrainingRepository extends JpaRepository<Training, Integer> {
}
