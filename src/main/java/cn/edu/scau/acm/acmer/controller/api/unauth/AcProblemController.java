package cn.edu.scau.acm.acmer.controller.api.unauth;

import cn.edu.scau.acm.acmer.model.PersonalProblemAcRank;
import cn.edu.scau.acm.acmer.service.ProblemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "api/unauth/acProblem", produces = "application/json; charset=utf-8")
public class AcProblemController {

    @Autowired
    private ProblemService problemService;

    @GetMapping("getPersonalProblemAcRank")
    List<PersonalProblemAcRank> getPersonalProblemAcRank(int grade, boolean includeRetired) {
        return problemService.getPersonalProblemAcRank(grade, includeRetired);
    }
}
