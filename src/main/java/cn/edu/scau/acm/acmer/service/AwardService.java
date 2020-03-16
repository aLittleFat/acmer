package cn.edu.scau.acm.acmer.service;

import cn.edu.scau.acm.acmer.entity.AwardView;
import org.springframework.data.domain.Page;

public interface AwardService {

    void addAward(String level, String regional, String time, String contestType, Integer teamId);

    void deleteAward(Integer awardId, String studentId) throws Exception;

    void deleteAward(Integer awardId) throws Exception;

    Page<AwardView> getAwardViewNotVerified(Integer page, Integer size);

    void verifyAward(Integer awardId) throws Exception;
}
