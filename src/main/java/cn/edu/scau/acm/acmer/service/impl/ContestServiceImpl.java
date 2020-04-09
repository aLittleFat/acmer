package cn.edu.scau.acm.acmer.service.impl;

import cn.edu.scau.acm.acmer.entity.*;
import cn.edu.scau.acm.acmer.model.ContestRecordLine;
import cn.edu.scau.acm.acmer.model.ContestTable;
import cn.edu.scau.acm.acmer.model.MultiContestRecordLine;
import cn.edu.scau.acm.acmer.repository.*;
import cn.edu.scau.acm.acmer.service.*;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class ContestServiceImpl implements ContestService {

    Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ContestRepository contestRepository;

    @Autowired
    private VjService vjService;

    @Autowired
    private HduService hduService;

    @Autowired
    private ContestProblemRepository contestProblemRepository;

    @Autowired
    private ContestRecordRepository contestRecordRepository;

    @Autowired
    private NowCoderService nowCoderService;

    @Autowired
    private JisuankeService jisuankeService;

    @Autowired
    private CfService cfService;

    @Autowired
    private OJService ojService;

    @Autowired
    private ContestRecordViewRepository contestRecordViewRepository;

    @Autowired
    private TeamService teamService;

    @Autowired
    private ProblemService problemService;

    @Autowired
    private TeamStudentRepository teamStudentRepository;

    @Autowired
    private ProblemRepository problemRepository;


    @Override
    @Transactional
    public void addContestRecord(String ojName, String cId, String studentId, Integer teamId, String account, String password) throws Exception {

        ojService.addOj(ojName);
        addContest(ojName, cId, account, password);
        Contest contest = contestRepository.findByOjNameAndCid(ojName, cId).get();
        if(contest.getEndTime().getTime() > System.currentTimeMillis() + 8*60*60*1000) {
            throw new Exception("比赛还未结束");
        }

        switch (ojName) {
            case "VJ": vjService.addContestRecord(ojName, cId, studentId, teamId, account, password); break;
            case "HDU": hduService.addContestRecord(ojName, cId, studentId, teamId, account, password); break;
            case "CodeForces":
            case "Gym": cfService.addContestRecord(ojName, cId, studentId, teamId, account);  break;
            case "计蒜客": jisuankeService.addContestRecord(ojName, cId, studentId, teamId, account);break;
            case "牛客": nowCoderService.addContestRecord(ojName, cId, studentId, teamId, account); break;
        }
    }

    @Override
    public void addContest(String ojName, String cId, String username, String password) throws Exception {
        Optional<Contest> optionalContest = contestRepository.findByOjNameAndCid(ojName, cId);
        if(optionalContest.isEmpty()) {
            switch (ojName) {
                case "VJ": vjService.addContest(ojName, cId, password); break;
                case "HDU": hduService.addContest(ojName, cId, username, password); break;
                case "CodeForces":
                case "Gym": cfService.addContest(ojName, cId); break;
                case "计蒜客": jisuankeService.addContest(ojName, cId);break;
                case "牛客": nowCoderService.addContest(ojName, cId); break;
                default: throw new Exception("不支持的OJ");
            }
        }
    }

    @Override
    public JSONObject getContestTableByStudentId(String studentId) {
        List<MultiContestRecordLine> multiContestRecordLines = contestRecordViewRepository.findAllMultiContestRecordLineByStudentId(studentId);
        return getContestRecordTable(multiContestRecordLines);
    }

    @Override
    public JSONObject getContestTableByTeamId(Integer teamId, String studentId) {
        List<MultiContestRecordLine> multiContestRecordLines = contestRecordViewRepository.findAllMultiContestRecordLineByTeamId(teamId);
        JSONObject res = getContestRecordTable(multiContestRecordLines);
        res.put("isMyTeam", teamService.checkInTeam(teamId, studentId));
        return res;
    }

    @Override
    public ContestTable getContestTableByContestId(Integer contestId) throws Exception {
        Optional<Contest> optionalContest = contestRepository.findById(contestId);
        if(optionalContest.isEmpty()) throw new Exception("不存在的竞赛");
        Contest contest = optionalContest.get();
        List<ContestRecordLine> contestRecordLines = contestRecordViewRepository.findAllContestRecordLineByContestId(contestId);
        Collections.sort(contestRecordLines);
        ContestTable contestTable = new ContestTable();
        contestTable.setContestRecordLines(contestRecordLines);
        contestTable.setTitle(contest.getTitle());
        contestTable.setProblemList(Arrays.asList(contest.getProblemList().split(" ")));
        return contestTable;
    }

    @Override
    public void updateUpSolved(ContestRecord contestRecord) {
        Contest contest = contestRepository.findById(contestRecord.getContestId()).get();
        TreeSet<String> upSolved = new TreeSet<>();
        TreeSet<String> solved = new TreeSet<>(Arrays.asList(contestRecord.getSolved().split(" ")));
        List<ContestProblem> contestProblems = contestProblemRepository.findAllByContestIdOrderByProblemIndexAsc(contest.getId());
        if(contestRecord.getTeamId() != null) {
            List<TeamStudent> teamStudents = teamStudentRepository.findAllByTeamId(contestRecord.getTeamId());
            for (ContestProblem contestProblem : contestProblems) {
                Problem problem = problemRepository.findById(contestProblem.getProblemId()).get();
                for (TeamStudent teamStudent : teamStudents) {
                    if (problemService.checkIsAc(problem.getId(), teamStudent.getStudentId())) {
                        if (!solved.contains(contestProblem.getProblemIndex())) {
                            upSolved.add(contestProblem.getProblemIndex());
                        }
                    }
                }
            }
        } else {
            for (ContestProblem contestProblem : contestProblems) {
                Problem problem = problemRepository.findById(contestProblem.getProblemId()).get();
                if (problemService.checkIsAc(problem.getId(), contestRecord.getStudentId())) {
                    if (!solved.contains(contestProblem.getProblemIndex())) {
                        upSolved.add(contestProblem.getProblemIndex());
                    }
                }
            }
        }
        contestRecord.setUpSolved(StringUtils.join(upSolved, " "));
        contestRecordRepository.save(contestRecord);
    }

    @Override
    public void changeSolution(Integer contestRecordId, String solution) throws Exception {
        ContestRecord contestRecord = contestRecordRepository.findById(contestRecordId).get();
        contestRecord.setSolution(solution);
        contestRecordRepository.save(contestRecord);
    }

    @Override
    public JSONObject getContestInfo(Integer contestId) {
        Contest contest = contestRepository.findById(contestId).get();
        JSONObject res = new JSONObject();
        res.put("title", contest.getTitle());
        String link;
        switch (contest.getOjName()) {
            case "VJ": link = "https://vjudge.net/contest/" + contest.getCid(); break;
            case "HDU": link = "http://acm.hdu.edu.cn/userloginex.php?cid=" + contest.getCid(); break;
            case "牛客": link = "https://ac.nowcoder.com/acm/contest/" + contest.getCid(); break;
            case "CodeForces": link = "https://codeforces.com/contest/" + contest.getCid(); break;
            case "Gym": link = "https://codeforces.com/gym/" + contest.getCid(); break;
            case "计蒜客": link = "https://www.jisuanke.com/contest/" + contest.getCid(); break;
            default:
                throw new IllegalStateException("Unexpected value: " + contest.getOjName());
        }
        res.put("link", link);
        return res;
    }

    @Override
    public void deleteContestRecord(Integer contestRecordId, String studentId) throws Exception {
        Optional<ContestRecord> optionalContestRecord = contestRecordRepository.findById(contestRecordId);
        if(optionalContestRecord.isEmpty()) {
            throw new Exception("不存在的比赛记录");
        }
        ContestRecord contestRecord = optionalContestRecord.get();
        if(contestRecord.getTeamId() != null) {
            if (!teamService.checkInTeam(contestRecord.getTeamId(), studentId)) {
                throw new Exception("不是自己队伍的比赛记录");
            }
        } else {
            if (!contestRecord.getStudentId().equals(studentId)) {
                throw new Exception("不是自己的比赛记录");
            }
        }
        contestRecordRepository.delete(contestRecord);
    }

    private JSONObject getContestRecordTable(List<MultiContestRecordLine> multiContestRecordLines){
        JSONObject res = new JSONObject();
        Set<String> problemList = new TreeSet<>();
        for(MultiContestRecordLine multiContestRecordLine : multiContestRecordLines) {
            problemList.addAll(multiContestRecordLine.getProblemList());
        }
        res.put("problemList", problemList);
        res.put("contestRecord", multiContestRecordLines);
        return res;
    }
}
