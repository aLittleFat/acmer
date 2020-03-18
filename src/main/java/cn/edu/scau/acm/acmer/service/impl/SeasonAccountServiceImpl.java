package cn.edu.scau.acm.acmer.service.impl;

import cn.edu.scau.acm.acmer.entity.SeasonAccount;
import cn.edu.scau.acm.acmer.entity.SeasonParticipantAccount;
import cn.edu.scau.acm.acmer.repository.SeasonAccountRepository;
import cn.edu.scau.acm.acmer.repository.SeasonParticipantAccountRepository;
import cn.edu.scau.acm.acmer.service.SeasonAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class SeasonAccountServiceImpl implements SeasonAccountService {

    @Autowired
    private SeasonAccountRepository seasonAccountRepository;

    @Autowired
    private SeasonParticipantAccountRepository seasonParticipantAccountRepository;

    @Override
    @Transactional
    public void addSeasonAccount(Integer seasonId, String title, List<Integer> seasonStudentIds, List<Integer> teamIds, List<String> handles, List<String> accounts, List<String> passwords) throws Exception {
        SeasonAccount seasonAccount = new SeasonAccount();
        seasonAccount.setTitle(title);
        seasonAccount.setSeasonId(seasonId);
        seasonAccount = seasonAccountRepository.save(seasonAccount);
        for (int i = 0; i < seasonStudentIds.size(); i++) {
            SeasonParticipantAccount seasonParticipantAccount = new SeasonParticipantAccount();
            seasonParticipantAccount.setSeasonAccountId(seasonAccount.getId());
            seasonParticipantAccount.setHandle(handles.get(i));
            seasonParticipantAccount.setAccount(accounts.get(i));
            seasonParticipantAccount.setPassword(passwords.get(i));
            seasonParticipantAccount.setSeasonStudentId(seasonStudentIds.get(i));
            seasonParticipantAccount.setTeamId(teamIds.get(i));
            seasonParticipantAccountRepository.save(seasonParticipantAccount);
        }
    }

    @Override
    public void deleteSeasonAccount(Integer seasonAccountId) throws Exception {
        Optional<SeasonAccount> optionalSeasonAccount = seasonAccountRepository.findById(seasonAccountId);
        if(optionalSeasonAccount.isEmpty()) {
            throw new Exception("不存在的账号集");
        }
        SeasonAccount seasonAccount = optionalSeasonAccount.get();
        seasonAccountRepository.delete(seasonAccount);
    }
}
