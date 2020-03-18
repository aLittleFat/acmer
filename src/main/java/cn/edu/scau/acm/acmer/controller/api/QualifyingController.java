package cn.edu.scau.acm.acmer.controller.api;

import cn.edu.scau.acm.acmer.entity.Qualifying;
import cn.edu.scau.acm.acmer.model.MyResponseEntity;
import cn.edu.scau.acm.acmer.service.QualifyingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "api", produces = "application/json; charset=utf-8")
public class QualifyingController {
    @Autowired
    private QualifyingService qualifyingService;

    @GetMapping("season/{seasonId}/qualifying")
    MyResponseEntity<List<Qualifying>> getQualifyingBySeasonId(@PathVariable Integer seasonId) {
        return new MyResponseEntity<>(qualifyingService.getBySeasonId(seasonId));
    }

    @PostMapping("season/{seasonId}/qualifying")
    MyResponseEntity<Void> addQualifyingBySeasonId(@PathVariable Integer seasonId, String title, String ojName, String cId, String password, Double proportion, Integer seasonAccountId) throws Exception {
        qualifyingService.addQualifying(seasonId, title, ojName, cId, password, proportion, seasonAccountId);
        return new MyResponseEntity<>();
    }
}
