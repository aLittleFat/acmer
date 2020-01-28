package cn.edu.scau.acm.acmer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AcmerApplication {

    public static void main(String[] args) {
        SpringApplication.run(AcmerApplication.class, args);
    }

}
