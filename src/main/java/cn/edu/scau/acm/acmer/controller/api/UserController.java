package cn.edu.scau.acm.acmer.controller.api;

import cn.edu.scau.acm.acmer.entity.User;
import cn.edu.scau.acm.acmer.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/User")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    public List<User> findAll(){
//        stringRedisTemplate.opsForValue().set("1478527245@qq.com_code", "123456");
//        System.out.println(stringRedisTemplate.opsForValue().get("1478527245@qq.com_code"));
        return userRepository.findAll();
    }
}
