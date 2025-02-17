package cn.edu.scau.acm.acmer;

import com.spring4all.swagger.EnableSwagger2Doc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableSwagger2Doc
@EnableAsync
public class AcmerApplication {

    public static void main(String[] args) {
        System.setProperty("webdriver.chrome.driver", "C:\\Driver\\chromedriver.exe");
        SpringApplication.run(AcmerApplication.class, args);
    }

}
