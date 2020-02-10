package cn.edu.scau.acm.acmer.service;

import cn.edu.scau.acm.acmer.model.PersonalContestRank;
import com.sun.istack.Nullable;

import java.util.List;

public interface ContestService {
    void addPersonalContestRecord(String ojName, String cId, String password, String studentId, String account) throws Exception;
    int addContest(String ojName, String ojId, String username, String password) throws Exception;
    List<PersonalContestRank> getPersonalContestRank(String studentId);
}
