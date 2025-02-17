package cn.edu.scau.acm.acmer.controller.api;

import cn.edu.scau.acm.acmer.entity.User;
import cn.edu.scau.acm.acmer.model.MyResponseEntity;
import cn.edu.scau.acm.acmer.model.StudentInfo;
import cn.edu.scau.acm.acmer.model.UserDto;
import cn.edu.scau.acm.acmer.repository.UserRepository;
import cn.edu.scau.acm.acmer.service.AccountService;
import cn.edu.scau.acm.acmer.service.StudentService;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api", produces = "application/json; charset=utf-8")
public class UserController {
    @Autowired
    AccountService accountService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentService studentService;

    @ApiOperation("获取目前登录用户的信息")
    @RequiresAuthentication()
    @GetMapping("info")
    MyResponseEntity<User> getMyInfo() {
        int id = ((UserDto) SecurityUtils.getSubject().getPrincipal()).getId();
        return new MyResponseEntity<>(userRepository.findById(id).get());
    }

    @ApiOperation("修改当前登录用户的个人信息，包括手机号码、icpc邮箱和QQ")
    @RequiresRoles({"student"})
    @PutMapping("info")
    MyResponseEntity<Void> changeMyInfo(String phone, String icpcEmail, String qq) {
        int id = ((UserDto) SecurityUtils.getSubject().getPrincipal()).getId();
        accountService.changeInfo(id, phone, icpcEmail, qq);
        return new MyResponseEntity<>();
    }

    @ApiOperation("获取学生概览页面的信息")
    @GetMapping("studentInfo")
    MyResponseEntity<StudentInfo> getStudentInfo(String studentId) {
        if(studentId == null || studentId.equals("")) {
            int id = ((UserDto) SecurityUtils.getSubject().getPrincipal()).getId();
            studentId = userRepository.findById(id).get().getStudentId();
        }
        return new MyResponseEntity<>(studentService.getStudentInfo(studentId));
    }

    @ApiOperation("队员申请退役")
    @PutMapping("retire/{studentId}")
    @RequiresRoles("student")
    MyResponseEntity<Void> retire(@PathVariable String studentId) throws Exception {
        accountService.retire(studentId);
        return new MyResponseEntity<>();
    }

    @ApiOperation("获取队员列表")
    @GetMapping("student")
    @RequiresRoles("admin")
    MyResponseEntity<Page<User>> getStudents(Integer page, Integer size) throws Exception {
        Pageable pr = PageRequest.of(page-1, size, Sort.Direction.DESC, "id");
        return new MyResponseEntity<>(userRepository.findAllByStudentIdNotNull(pr));
    }

    @ApiOperation("删除队员")
    @DeleteMapping("student")
    @RequiresRoles("admin")
    MyResponseEntity<Void> deleteStudents(String studentId) throws Exception {
        String myStudentId = null;
        try {
            int id = ((UserDto) SecurityUtils.getSubject().getPrincipal()).getId();
            myStudentId = userRepository.findById(id).get().getStudentId();
        } catch (Exception ignored) {}
        accountService.deleteStudents(studentId, myStudentId);
        return new MyResponseEntity<>();
    }

    @ApiOperation("设为管理员")
    @PutMapping("admin")
    @RequiresRoles("admin")
    MyResponseEntity<Void> setAdmin(String studentId) throws Exception {
        accountService.setAdmin(studentId);
        return new MyResponseEntity<>();
    }

}
