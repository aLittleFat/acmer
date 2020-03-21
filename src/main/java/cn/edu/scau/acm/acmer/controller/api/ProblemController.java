package cn.edu.scau.acm.acmer.controller.api;

import cn.edu.scau.acm.acmer.entity.ProblemView;
import cn.edu.scau.acm.acmer.model.MyResponseEntity;
import cn.edu.scau.acm.acmer.model.UserDto;
import cn.edu.scau.acm.acmer.repository.TagRepository;
import cn.edu.scau.acm.acmer.repository.UserRepository;
import cn.edu.scau.acm.acmer.service.ProblemDifficultService;
import cn.edu.scau.acm.acmer.service.ProblemService;
import cn.edu.scau.acm.acmer.service.ProblemTagService;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping(value = "api", produces = "application/json; charset=utf-8")
public class ProblemController {

    @Autowired
    private ProblemService problemService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProblemTagService problemTagService;

    @Autowired
    private ProblemDifficultService problemDifficultService;

    @Autowired
    private TagRepository tagRepository;

    @ApiOperation("获取题目的信息，包括评价和标签")
    @GetMapping("problemInfo")
    MyResponseEntity<JSONObject> getProblemInfo(Integer problemId) {
        String myStudentId = null;
        try {
            int id = ((UserDto) SecurityUtils.getSubject().getPrincipal()).getId();
            myStudentId = userRepository.findById(id).get().getStudentId();
        } catch (Exception ignored) { }
        return new MyResponseEntity<>(problemService.getProblemInfo(problemId, myStudentId));
    }

    @ApiOperation("修改我对题目的难度评价")
    @RequiresRoles("student")
    @PutMapping("problemDifficult")
    MyResponseEntity<Void> updateProblemDifficult(Integer problemId, BigDecimal difficult) throws Exception {
        int id = ((UserDto) SecurityUtils.getSubject().getPrincipal()).getId();
        String myStudentId = userRepository.findById(id).get().getStudentId();
        problemDifficultService.updateProblemDifficult(problemId, myStudentId, difficult);
        return new MyResponseEntity<>();
    }

    @ApiOperation("添加题目标签")
    @RequiresRoles("student")
    @PutMapping("problemTag")
    MyResponseEntity<Void> updateProblemTag(Integer problemId, String tagName) throws Exception {
        int id = ((UserDto) SecurityUtils.getSubject().getPrincipal()).getId();
        String myStudentId = userRepository.findById(id).get().getStudentId();
        problemTagService.updateProblemTag(problemId, myStudentId, tagName);
        return new MyResponseEntity<>();
    }

    @ApiOperation("删除题目标签")
    @RequiresRoles("student")
    @DeleteMapping("problemTag")
    MyResponseEntity<Void> deleteProblemTag(Integer problemId, String tagName) throws Exception {
        int id = ((UserDto) SecurityUtils.getSubject().getPrincipal()).getId();
        String myStudentId = userRepository.findById(id).get().getStudentId();
        problemTagService.deleteProblemTag(problemId, myStudentId, tagName);
        return new MyResponseEntity<>();
    }

    @GetMapping("tag")
    MyResponseEntity<List<String>> searchTagsByKey(String key) {
        return new MyResponseEntity<>(tagRepository.findAllLike("%" + key + "%"));
    }

    @GetMapping("problemView")
    MyResponseEntity<Page<ProblemView>> searchProblem(String key, BigDecimal minDifficult,  BigDecimal maxDifficult, String tagName, Integer page, Integer size) {
        return new MyResponseEntity<>(problemService.searchProblem(key, minDifficult, maxDifficult, tagName, page, size));
    }
}
