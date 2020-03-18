package cn.edu.scau.acm.acmer.service;

import java.util.List;

public interface SeasonAccountService {
    void addSeasonAccount(Integer seasonId, String title, List<Integer> seasonStudentIds, List<Integer> teamIds, List<String> handles, List<String> accounts, List<String> passwords) throws Exception;

    void deleteSeasonAccount(Integer seasonAccountId) throws Exception;
}
