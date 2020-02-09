package cn.edu.scau.acm.acmer.service.impl;

import cn.edu.scau.acm.acmer.model.MyResponseEntity;
import cn.edu.scau.acm.acmer.service.AccountService;
import cn.edu.scau.acm.acmer.service.ScauCfService;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

@Service
public class ScauCfServiceImpl implements ScauCfService {

    Logger log = LoggerFactory.getLogger(this.getClass());

    @Value("${scau.cf.username}")
    private String username;

    @Value("${scau.cf.password}")
    private String password;

    @Value("${scau.cf.url}")
    private String URL;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private AccountService accountService;

    @Override
    public String login() {
        try {
            String url = URL + "interfaces/app_login_API";
            HttpHeaders headers = new HttpHeaders();
            JSONObject json = new JSONObject();
            json.put("username", username);
            json.put("password", password);
            log.info(json.toJSONString());
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> request = new HttpEntity<>(json.toString(), headers);
            JSONObject response = restTemplate.postForObject(url, request, JSONObject.class);
            if(response.getInteger("status") == 0)
                return response.getString("token");
            else return null;
        }
        catch (Exception e) {
            return null;
        }
    }

    @Override
    public void sendCfVerifyCode(String cfHandle) throws Exception {
        String token = login();
        if(token == null) throw new Exception("服务器出错");

        String verifyCode = stringRedisTemplate.opsForValue().get(cfHandle + "_Verify");
        if(verifyCode == null){
            verifyCode = accountService.genVerifyCode();
            stringRedisTemplate.opsForValue().set(cfHandle + "_Verify", verifyCode, 10, TimeUnit.MINUTES);
        }

        String url = URL + "interfaces/app_send_codeforces_captcha_API";
        HttpHeaders headers = new HttpHeaders();
        JSONObject json = new JSONObject();
        json.put("handle", cfHandle);
        json.put("token", token);
        String content = "Your handle is being linked to the SCAU_ACMERsystem. The user name is " + cfHandle + ". Your verify code is: \'" + verifyCode + "\' .If the operator is not yourself, please ignore this message. The link is available in 10 mins.";
        json.put("content", content);
        json.put("captcha", verifyCode);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(json.toString(), headers);
        JSONObject response = restTemplate.postForObject(url, request, JSONObject.class);
        int status = response.getInteger("status");
        switch (status) {
            case 0: break;
            case 5: throw new Exception("cf用户名出错");
            case 7: throw new Exception("发送失败");
            default: throw new Exception("服务器出错");
        }
    }
}
