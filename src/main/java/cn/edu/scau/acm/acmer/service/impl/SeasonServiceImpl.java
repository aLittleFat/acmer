package cn.edu.scau.acm.acmer.service.impl;

import cn.edu.scau.acm.acmer.entity.Season;
import cn.edu.scau.acm.acmer.repository.SeasonRepository;
import cn.edu.scau.acm.acmer.service.SeasonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SeasonServiceImpl implements SeasonService {

    @Autowired
    private SeasonRepository seasonRepository;

    @Override
    public List<Season> getAllSeason() {
        return seasonRepository.findAll();
    }

    @Override
    public void addSeason(int year, String name, String type, int cfPro) throws Exception {
        if(cfPro < 0 || cfPro > 100) {
            throw new Exception("cf比例越界");
        }
        Season season = new Season();
        season.setYear(year);
        season.setName(name);
        season.setType(type);
        season.setCfProportion(cfPro / 100.0);
        seasonRepository.save(season);
    }

    @Override
    public void deleteSeason(int seasonId) throws Exception {
        Optional<Season> season = seasonRepository.findById(seasonId);
        if(season.isEmpty()) {
            throw new Exception("不存在该赛季");
        }
        //todo 删除season连接的东西
        seasonRepository.delete(season.get());
    }
}
