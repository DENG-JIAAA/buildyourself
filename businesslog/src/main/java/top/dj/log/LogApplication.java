package top.dj.log;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// @SpringBootApplication
@SpringBootApplication(exclude = DruidDataSourceAutoConfigure.class)
@MapperScan("top.dj.log.mapper")
public class LogApplication {
    public static void main(String[] args) {
        SpringApplication.run(LogApplication.class, args);
    }
}
