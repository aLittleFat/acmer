package cn.edu.scau.acm.acmer.service.impl;

import cn.edu.scau.acm.acmer.entity.Contest;
import cn.edu.scau.acm.acmer.entity.ContestProblem;
import cn.edu.scau.acm.acmer.repository.ContestProblemRepository;
import cn.edu.scau.acm.acmer.repository.ContestRepository;
import cn.edu.scau.acm.acmer.service.ContestService;
import cn.edu.scau.acm.acmer.service.HduService;
import cn.edu.scau.acm.acmer.service.VjService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ContestServiceImpl implements ContestService {

    @Autowired
    private ContestRepository contestRepository;

    @Autowired
    private VjService vjService;

    @Autowired
    private HduService hduService;

    @Autowired
    private ContestProblemRepository contestProblemRepository;


    @Override
    public String addContestRecord(String ojName, String ojId, String password, String studentId, String account) {

        return null;
    }

    @Override
    public void addContest(String ojName, String ojId, String username, String password) throws Exception {
        if(contestRepository.findByOjNameAndOjid(ojName, ojId).isPresent()) return;
        switch (ojName) {
            case "VJ": vjService.addContest(ojId, password); break;
            case "HDU": hduService.addContest(ojId, username, password); break;
            case "CodeForces": break;
            case "计蒜客": break;
            case "牛客": break;
            default: throw new Exception("不存在的OJ名");
        }
    }

    @Override
    public void addContestProblem(int contestId, String idInContest, Integer problemId) {
        ContestProblem contestProblem = new ContestProblem();
        contestProblem.setContestId(contestId);
        contestProblem.setiDinContest(idInContest);
        contestProblem.setProblemId(problemId);
        contestProblemRepository.save(contestProblem);
    }

}
