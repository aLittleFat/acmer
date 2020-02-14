package cn.edu.scau.acm.acmer.service.impl;

import cn.edu.scau.acm.acmer.entity.OjAccount;
import cn.edu.scau.acm.acmer.entity.Student;
import cn.edu.scau.acm.acmer.entity.User;
import cn.edu.scau.acm.acmer.repository.OjAccountRepository;
import cn.edu.scau.acm.acmer.repository.ProblemAcRecordRepository;
import cn.edu.scau.acm.acmer.repository.StudentRepository;
import cn.edu.scau.acm.acmer.repository.UserRepository;
import cn.edu.scau.acm.acmer.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class OJAccountServiceImpl implements OJAccountService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private OjAccountRepository ojAccountRepository;

    @Autowired
    private OJService ojService;

    @Autowired
    private VjService vjService;

    @Autowired
    private HduService hduService;

    @Autowired
    private BzojService bzojService;

    @Autowired
    private CfService cfService;

    @Autowired
    private ProblemAcRecordRepository problemAcRecordRepository;

    @Override
    public boolean checkOjAccount(String ojName, String username, String password) {
        if(ojName.equals("VJ")) {
            return vjService.checkVjAccount(username, password);
        } else if (ojName.equals("HDU")) {
            return hduService.checkHduAccount(username, password);
        } else if (ojName.equals("BZOJ")) {
            return bzojService.checkBzojAccount(username, password);
        } else if (ojName.equals("CodeForces")) {
            return cfService.checkCfAccount(username, password);
        }
        return false;
    }

    @Override
    public void addOjAccount(String ojName, String username, String password, int id) throws Exception {
        User u = userRepository.findById(id).get();
        Student stu = studentRepository.findByUserId(u.getId()).get();

        ojService.addOj(ojName);

        if (ojAccountRepository.findByStudentIdAndOjName(stu.getId(), ojName).isPresent()) {
            throw new Exception("已存在" + ojName + "账号");
        }
        if (checkOjAccount(ojName, username, password)) {
            OjAccount ojAccount = new OjAccount();
            ojAccount.setAccount(username);
            ojAccount.setOjName(ojName);
            ojAccount.setStudentId(stu.getId());
            ojAccountRepository.save(ojAccount);
        } else {
            throw new Exception("添加失败，用户名/密码/网络错误");
        }
    }

    @Override
    public String getOjAccount(String ojName, int userId) {
        Student stu = studentRepository.findByUserId(userId).get();
        Optional<OjAccount> ojAccount = ojAccountRepository.findByStudentIdAndOjName(stu.getId(), ojName);
        if (ojAccount.isEmpty()) {
            return "";
        } else {
            return  ojAccount.get().getAccount();
        }
    }

    @Override
    @Transactional
    public void deleteOjAccount(String ojName, int userId) throws Exception {
        Student stu = studentRepository.findByUserId(userId).get();
        Optional<OjAccount> ojAccount = ojAccountRepository.findByStudentIdAndOjName(stu.getId(), ojName);
        if (ojAccount.isEmpty()) throw new Exception("VJ账号不存在");
        problemAcRecordRepository.deleteAllByOjAccountId(ojAccount.get().getId());
        ojAccountRepository.delete(ojAccount.get());
    }

    @Override
    @Transactional
    public void changeOjAccount(String ojName, String username, String password, int userId) throws Exception {
        Student stu = studentRepository.findByUserId(userId).get();
        Optional<OjAccount> ojAccount = ojAccountRepository.findByStudentIdAndOjName(stu.getId(), ojName);
        if (ojAccount.isEmpty()) {
            throw new Exception("目前不存在" + ojName + "账户");
        }
        if (ojAccount.get().getAccount().equals(username)) {
            throw new Exception("你修改的用户名和之前的一样，无需修改");
        }
        if (checkOjAccount(ojName, username, password)) {
            problemAcRecordRepository.deleteAllByOjAccountId(ojAccount.get().getId());
            OjAccount newOjAccount = ojAccount.get();
            newOjAccount.setAccount(username);
            ojAccountRepository.save(newOjAccount);
        } else {
            throw new Exception("修改失败，用户名或密码错误");
        }
    }
}
