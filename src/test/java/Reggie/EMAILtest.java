package Reggie;


import Reggie.utils.VerificationCodeGenerator;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Slf4j
@SpringBootTest
class EMAILtest {

    @Autowired
    JavaMailSenderImpl mailSender;

    @Autowired
    private VerificationCodeGenerator verificationCodeGenerator;

    @Value("${spring.mail.username}")
    private String username;


    @Test
    public void contextLoads() {
        SimpleMailMessage message = new SimpleMailMessage();
        String code = verificationCodeGenerator.generate(6);
        long timestamp = System.currentTimeMillis(); // 当前时间戳
        message.setSubject("验证码");
        message.setText("你收到的验证码是：" + code + "\n验证码一次有效，五分钟后失效。\n发送时间：" + LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        message.setTo("1044867745@qq.com"); //收件人邮箱地址,请自行修改
        message.setFrom(username);
        mailSender.send(message);
    }
}