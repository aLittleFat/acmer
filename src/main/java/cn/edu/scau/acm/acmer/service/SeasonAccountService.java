package cn.edu.scau.acm.acmer.service;

import cn.edu.scau.acm.acmer.entity.SeasonAccount;

import java.util.List;

public interface SeasonAccountService {
    void addSeasonAccount(Integer seasonId, String title, List<Integer> seasonStudentIds, List<Integer> teamIds, List<String> handles, List<String> accounts, List<String> passwords) throws Exception;

    void deleteSeasonAccount(Integer seasonAccountId) throws Exception;

    SeasonAccount getSeasonAccount(Integer seasonAccountId) throws Exception;
}
