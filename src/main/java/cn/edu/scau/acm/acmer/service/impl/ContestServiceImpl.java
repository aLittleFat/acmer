package cn.edu.scau.acm.acmer.service.impl;

import cn.edu.scau.acm.acmer.entity.Contest;
import cn.edu.scau.acm.acmer.repository.ContestRepository;
import cn.edu.scau.acm.acmer.service.ContestService;
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


    @Override
    public String addContestRecord(String ojName, String ojId, String password, String studentId, String account) {

        return null;
    }

    @Override
    public String addContest(String ojName, String ojId, String password) throws Exception {
        if(contestRepository.findByOjNameAndOjid(ojName, ojId).isPresent()) return "true";
        switch (ojName) {
            case "VJ": vjService.addContest(ojId, password); break;
            case "HDU": break;
            case "CodeForces": break;
            case "计蒜客": break;
            case "牛客": break;
        }
        return "不存在的OJ名";
    }

}
