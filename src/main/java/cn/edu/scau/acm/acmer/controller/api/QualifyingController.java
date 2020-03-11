package cn.edu.scau.acm.acmer.controller.api;

import cn.edu.scau.acm.acmer.entity.Qualifying;
import cn.edu.scau.acm.acmer.model.MyResponseEntity;
import cn.edu.scau.acm.acmer.service.QualifyingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "api", produces = "application/json; charset=utf-8")
public class QualifyingController {
    @Autowired
    private QualifyingService qualifyingService;

    @GetMapping("qualifying")
    MyResponseEntity<List<Qualifying>> getQualifyingBySeasonId(Integer seasonId) {
        return new MyResponseEntity<>(qualifyingService.getBySeasonId(seasonId));
    }
}
