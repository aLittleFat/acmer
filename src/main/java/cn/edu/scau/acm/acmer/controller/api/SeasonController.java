package cn.edu.scau.acm.acmer.controller.api;

import cn.edu.scau.acm.acmer.entity.Season;
import cn.edu.scau.acm.acmer.model.MyResponseEntity;
import cn.edu.scau.acm.acmer.service.SeasonService;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "api", produces = "application/json; charset=utf-8")
public class SeasonController {

    @Autowired
    private SeasonService seasonService;

    @GetMapping("/season")
    MyResponseEntity<List<Season>> getSeason() {
        return new MyResponseEntity<>(seasonService.getAllSeason());
    }

    @RequiresRoles({"admin"})
    @PostMapping("/season")
    MyResponseEntity<Void> addSeason(int year, String name, String type, int cfPro) throws Exception {
        seasonService.addSeason(year, name, type, cfPro);
        return new MyResponseEntity<>();
    }


    @RequiresRoles({"admin"})
    @DeleteMapping("/season")
    MyResponseEntity<Void> deleteSeason(int seasonId) throws Exception {
        seasonService.deleteSeason(seasonId);
        return new MyResponseEntity<>();
    }
}
