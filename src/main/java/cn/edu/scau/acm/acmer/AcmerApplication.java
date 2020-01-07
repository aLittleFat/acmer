package cn.edu.scau.acm.acmer;

import com.spring4all.swagger.EnableSwagger2Doc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableSwagger2Doc
public class AcmerApplication {

    public static void main(String[] args) {
        SpringApplication.run(AcmerApplication.class, args);
    }

}
