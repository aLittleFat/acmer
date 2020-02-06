package cn.edu.scau.acm.acmer.service.impl;

import cn.edu.scau.acm.acmer.entity.OJAccount;
import cn.edu.scau.acm.acmer.entity.Student;
import cn.edu.scau.acm.acmer.entity.User;
import cn.edu.scau.acm.acmer.repository.OJAccountRepository;
import cn.edu.scau.acm.acmer.repository.ProblemACRecordRepository;
import cn.edu.scau.acm.acmer.repository.StudentRepository;
import cn.edu.scau.acm.acmer.repository.UserRepository;
import cn.edu.scau.acm.acmer.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OJAccountServiceImpl implements OJAccountService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private OJAccountRepository ojAccountRepository;

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
    private ProblemACRecordRepository problemACRecordRepository;

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
    public String addOjAccount(String ojName, String username, String password, int id) {

        User u = userRepository.findById(id);
        Student stu = studentRepository.findByUserId(u.getId());

        ojService.addOj(ojName);

        if(ojAccountRepository.findByStudentIdAndOjName(stu.getId(), ojName) != null) {
            return "已存在" + ojName + "账号";
        }
        if(checkOjAccount(ojName, username, password)){
            OJAccount ojAccount = new OJAccount();
            ojAccount.setAccount(username);
            ojAccount.setOjName(ojName);
            ojAccount.setStudentId(stu.getId());
            ojAccountRepository.save(ojAccount);
            return "true";
        }
        else {
            return "添加失败，用户名/密码/网络错误";
        }
    }

    @Override
    public String getOjAccount(String ojName, int userId) {
        Student stu = studentRepository.findByUserId(userId);
        OJAccount ojAccount = ojAccountRepository.findByStudentIdAndOjName(stu.getId(), ojName);
        if(ojAccount == null) {
            return "";
        }
        else{
            return ojAccount.getAccount();
        }
    }

    @Override
    public String deleteOjAccount(String ojName, int userId) {
        Student stu = studentRepository.findByUserId(userId);
        OJAccount ojAccount = ojAccountRepository.findByStudentIdAndOjName(stu.getId(), ojName);
        if(ojAccount == null) {
            return "VJ账号不存在";
        }

        problemACRecordRepository.deleteAllByOjAccountId(ojAccount.getId());

        ojAccountRepository.delete(ojAccount);
        return "true";
    }

    @Override
    public String changeOjAccount(String ojName, String username, String password, int userId) {
        Student stu = studentRepository.findByUserId(userId);
        OJAccount ojAccount = ojAccountRepository.findByStudentIdAndOjName(stu.getId(), ojName);
        if(ojAccount == null) {
            return "目前不存在" + ojName + "账户";
        }
        if(ojAccount.getAccount().equals(username)){
            return "你修改的用户名和之前的一样，无需修改";
        }
        if(checkOjAccount(ojName, username, password)){
            deleteOjAccount(ojName, userId);
            OJAccount newOjAccount = new OJAccount();
            newOjAccount.setAccount(username);
            newOjAccount.setOjName(ojName);
            newOjAccount.setStudentId(stu.getId());
            ojAccountRepository.save(newOjAccount);
            return "true";
        } else {
            return "修改失败，用户名/密码/网络错误";
        }
    }
}
