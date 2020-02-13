package cn.edu.scau.acm.acmer.service;

import cn.edu.scau.acm.acmer.entity.Season;

import java.util.List;

public interface SeasonService {
    List<Season> getAllSeason();

    void addSeason(int year, String name, String type, int cfPro) throws Exception;

    void deleteSeason(int seasonId) throws Exception;
}
