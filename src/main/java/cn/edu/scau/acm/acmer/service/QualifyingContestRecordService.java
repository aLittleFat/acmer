package cn.edu.scau.acm.acmer.service;

import org.springframework.scheduling.annotation.Async;

public interface QualifyingContestRecordService {
    @Async
    void updateQualifyingContestRecord(Integer qualifyingId);

    void updatePersonalQualifyingContestRecord(Integer qualifyingId);

    void updateTeamQualifyingContestRecord(Integer qualifyingId);
}
