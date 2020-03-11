package cn.edu.scau.acm.acmer.service;

import cn.edu.scau.acm.acmer.entity.Qualifying;

import java.util.List;

public interface QualifyingService {
    List<Qualifying> getBySeasonId(Integer seasonId);
}
