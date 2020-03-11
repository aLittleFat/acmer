package cn.edu.scau.acm.acmer.service.impl;

import cn.edu.scau.acm.acmer.entity.Qualifying;
import cn.edu.scau.acm.acmer.repository.QualifyingRepository;
import cn.edu.scau.acm.acmer.service.QualifyingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QualifyingServiceImpl implements QualifyingService {
    @Autowired
    private QualifyingRepository qualifyingRepository;

    @Override
    public List<Qualifying> getBySeasonId(Integer seasonId) {
        return qualifyingRepository.findAllBySeasonId(seasonId);
    }
}
