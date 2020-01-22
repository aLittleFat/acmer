package cn.edu.scau.acm.acmer.service.impl;

import cn.edu.scau.acm.acmer.entity.OJAccount;
import cn.edu.scau.acm.acmer.entity.Student;
import cn.edu.scau.acm.acmer.entity.User;
import cn.edu.scau.acm.acmer.repository.OJAccountRepository;
import cn.edu.scau.acm.acmer.repository.OJRepository;
import cn.edu.scau.acm.acmer.repository.StudentRepository;
import cn.edu.scau.acm.acmer.repository.UserRepository;
import cn.edu.scau.acm.acmer.service.OJAccountService;
import cn.edu.scau.acm.acmer.service.OJService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class OJAccountServiceImpl implements OJAccountService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private OJRepository ojRepository;

    @Autowired
    private OJAccountRepository ojAccountRepository;

    @Autowired
    private OJService ojService;

    @Override
    public String addVjAccount(String username, String password, int id) {

        User u = userRepository.findById(id);
        Student stu = studentRepository.findByUserId(u.getId());

        ojService.addOj("VJ");

        if(ojAccountRepository.findByStudentIdAndOjName(stu.getId(), "VJ") != null) {
            return "已存在VJ账号";
        }

        if(checkVjLoginStatus()){
            vjLogout();
        }
        String url = "https://vjudge.net/user/login";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        map.add("username", username);
        map.add("password", password);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        String response = restTemplate.postForObject( url, request , String.class );
        log.info(response);
        if(response.equals("success")){
            OJAccount ojAccount = new OJAccount();
            ojAccount.setAccount(username);
            ojAccount.setOjName("VJ");
            ojAccount.setStudentId(stu.getId());
            ojAccountRepository.save(ojAccount);
            return "true";
        }
        else{
            return response;
        }
    }

    @Override
    public boolean checkVjLoginStatus() {
        String url = "https://vjudge.net/user/checkLogInStatus";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        log.info(request.toString());
        String res = restTemplate.postForObject(url, request, String.class);
        log.info(res);
        return res.equals("true");
    }

    @Override
    public void vjLogout() {
        String url = "https://vjudge.net/user/logout";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        ResponseEntity<String> response = restTemplate.postForEntity( url, request , String.class );
    }
}
