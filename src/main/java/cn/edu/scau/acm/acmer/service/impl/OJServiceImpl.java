package cn.edu.scau.acm.acmer.service.impl;

import cn.edu.scau.acm.acmer.entity.OJ;
import cn.edu.scau.acm.acmer.repository.OJRepository;
import cn.edu.scau.acm.acmer.service.OJService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OJServiceImpl implements OJService {

    @Autowired
    OJRepository ojRepository;

    @Override
    public void addOj(String name) {
        if(ojRepository.findByName(name) != null) return;
        OJ oj = new OJ();
        oj.setName(name);
        ojRepository.save(oj);
    }
}
