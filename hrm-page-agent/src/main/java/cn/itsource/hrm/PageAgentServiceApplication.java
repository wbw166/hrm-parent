package cn.itsource.hrm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class PageAgentServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(PageAgentServiceApplication.class,args);
    }
}
