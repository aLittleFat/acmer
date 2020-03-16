package cn.edu.scau.acm.acmer.service.impl;

import cn.edu.scau.acm.acmer.entity.Award;
import cn.edu.scau.acm.acmer.entity.AwardView;
import cn.edu.scau.acm.acmer.repository.AwardRepository;
import cn.edu.scau.acm.acmer.repository.AwardViewRepository;
import cn.edu.scau.acm.acmer.service.AwardService;
import cn.edu.scau.acm.acmer.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Optional;

@Service
public class AwardServiceImpl implements AwardService {

    @Autowired
    private AwardRepository awardRepository;

    @Autowired
    private TeamService teamService;

    @Autowired
    private AwardViewRepository awardViewRepository;

    @Override
    public void addAward(String level, String regional, String time, String contestType, Integer teamId) {
        time = time.substring(0, time.indexOf('.')).replace('T', ' ');
        Timestamp timestamp = new Timestamp(Timestamp.valueOf(time).getTime() + 16*60*60*1000);
        Award award = new Award();
        award.setContestType(contestType);
        award.setLevel(level);
        award.setRegional(regional);
        award.setTeamId(teamId);
        award.setTime(timestamp);
        award.setVerified((byte) 0);
        awardRepository.save(award);
    }

    @Override
    public void deleteAward(Integer awardId, String studentId) throws Exception {
        Optional<Award> optionalAward = awardRepository.findById(awardId);
        if(optionalAward.isEmpty()) {
            throw new Exception("不存在的获奖记录");
        }
        Award award = optionalAward.get();
        if(!teamService.checkInTeam(award.getTeamId(), studentId)) {
            throw new Exception("不属于该队伍");
        }
        awardRepository.delete(award);
    }

    @Override
    public void deleteAward(Integer awardId) throws Exception {
        Optional<Award> optionalAward = awardRepository.findById(awardId);
        if(optionalAward.isEmpty()) {
            throw new Exception("不存在的获奖记录");
        }
        Award award = optionalAward.get();
        awardRepository.delete(award);
        teamService.sendMail(award.getTeamId(), "申报奖项不通过通知", "你的队伍申报的奖项" + award.getContestType() + award.getRegional() + "赛区" + award.getLevel() + "奖审核不通过，奖项记录已被删除。");
    }

    @Override
    public Page<AwardView> getAwardViewNotVerified(Integer page, Integer size) {
        Pageable pr = PageRequest.of(page-1, size, Sort.Direction.ASC, "time");
        return awardViewRepository.findAllByVerified(pr, (byte)0);
    }

    @Override
    public void verifyAward(Integer awardId) throws Exception {
        Optional<Award> optionalAward = awardRepository.findById(awardId);
        if(optionalAward.isEmpty()) {
            throw new Exception("不存在的获奖记录");
        }
        Award award = optionalAward.get();
        award.setVerified((byte) 1);
        awardRepository.save(award);
    }
}
