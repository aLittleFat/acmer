package cn.edu.scau.acm.acmer.service.impl;

import cn.edu.scau.acm.acmer.entity.ProblemTag;
import cn.edu.scau.acm.acmer.repository.ProblemTagRepository;
import cn.edu.scau.acm.acmer.service.ProblemService;
import cn.edu.scau.acm.acmer.service.ProblemTagService;
import cn.edu.scau.acm.acmer.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProblemTagServiceImpl implements ProblemTagService {

    @Autowired
    private TagService tagService;

    @Autowired
    private ProblemService problemService;

    @Autowired
    private ProblemTagRepository problemTagRepository;

    @Override
    public void updateProblemTag(Integer problemId, String studentId, String tagName) throws Exception {
        tagService.addTag(tagName);
        if(!problemService.checkIsAc(problemId, studentId)) {
            throw new Exception("没有AC此题");
        }
        Optional<ProblemTag> optionalProblemTag = problemTagRepository.findByProblemIdAndStudentIdAndTagName(problemId, studentId, tagName);
        if(optionalProblemTag.isPresent()) {
            throw new Exception("已经存在该标签");
        }
        ProblemTag problemTag = new ProblemTag();
        problemTag.setProblemId(problemId);
        problemTag.setStudentId(studentId);
        problemTag.setTagName(tagName);
        problemTagRepository.save(problemTag);
    }

    @Override
    public void deleteProblemTag(Integer problemId, String studentId, String tagName) throws Exception {
        Optional<ProblemTag> optionalProblemTag = problemTagRepository.findByProblemIdAndStudentIdAndTagName(problemId, studentId, tagName);
        if(optionalProblemTag.isEmpty()) {
            throw new Exception("不存在的标签");
        }
        problemTagRepository.delete(optionalProblemTag.get());
    }
}
