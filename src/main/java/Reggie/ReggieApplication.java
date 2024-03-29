package Reggie;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@Slf4j
@ServletComponentScan
@EnableTransactionManagement
public class ReggieApplication {

    public static void main(String[] args) {

        SpringApplication.run(ReggieApplication.class, args);
        log.info("\n管理端：http://localhost:7080/backend/page/login/login.html");
        log.info("\n移动端：http://localhost:7080/front/page/login.html");
    }
}
