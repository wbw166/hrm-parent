package cn.itsource.hrm;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@MapperScan("cn.itsource.hrm.mapper")
@EnableFeignClients
public class PageServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(PageServiceApplication.class,args);
    }
}
