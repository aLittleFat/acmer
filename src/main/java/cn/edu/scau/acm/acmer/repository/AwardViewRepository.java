package cn.edu.scau.acm.acmer.repository;

import cn.edu.scau.acm.acmer.entity.AwardView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AwardViewRepository extends JpaRepository<AwardView, Integer> {
    Page<AwardView> findAllByVerified(Pageable pageable, Byte verified);
}
