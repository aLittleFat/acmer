package cn.edu.scau.acm.acmer.service.impl;

import cn.edu.scau.acm.acmer.entity.Tag;
import cn.edu.scau.acm.acmer.repository.TagRepository;
import cn.edu.scau.acm.acmer.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TagServiceImpl implements TagService {
    @Autowired
    private TagRepository tagRepository;

    @Override
    public void addTag(String tagName) throws Exception {
        if(tagName.equals("")) {
            throw new Exception("标签名不能为空");
        }
        if(tagRepository.findById(tagName).isEmpty()) {
            Tag tag = new Tag();
            tag.setName(tagName);
            tagRepository.save(tag);
        }
    }
}
