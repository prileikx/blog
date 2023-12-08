package cn.devsp.blog;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("cn.devsp.blog.dao")
public class Blog2Application {

    public static void main(String[] args) {
        SpringApplication.run(Blog2Application.class, args);
    }

}
