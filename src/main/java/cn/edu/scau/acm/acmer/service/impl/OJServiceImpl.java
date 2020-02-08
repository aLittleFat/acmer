package cn.edu.scau.acm.acmer.service.impl;

import cn.edu.scau.acm.acmer.entity.Oj;
import cn.edu.scau.acm.acmer.repository.OjRepository;
import cn.edu.scau.acm.acmer.service.OJService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OJServiceImpl implements OJService {

    @Autowired
    private OjRepository ojRepository;

    @Override
    public void addOj(String name) {
        if(ojRepository.findByName(name).isPresent()) return;
        Oj oj = new Oj();
        oj.setName(name);
        ojRepository.save(oj);
    }
}
