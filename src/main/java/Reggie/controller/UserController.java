package Reggie.controller;

import Reggie.common.Result;
import Reggie.pojo.User;
import Reggie.service.UserService;
import Reggie.utils.VerificationCodeGenerator;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;

@Controller
@Slf4j
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private VerificationCodeGenerator codeGenerator;

    @Autowired
    JavaMailSenderImpl mailSender;

    @Value("${spring.mail.username}")
    private String username;

    private static final long CODE_INTERVAL = 30 * 1000; //验证码间隔时间，单位为毫秒
    private static final long CODE_EXPIRE = 5 * 60 * 1000; //验证码有效时间，单位为毫秒


    @PostMapping("/sendMsg")
    private Result<String> sendMsg(@RequestBody User user, HttpSession session){
        String phone = user.getPhone();
        if (StringUtils.isNotEmpty(phone)){
            //检查时间间隔
            long now = System.currentTimeMillis();
            String lastTimeStr = (String) session.getAttribute(phone + "_time");
            if (lastTimeStr != null) {
                long lastTime = Long.parseLong(lastTimeStr);
                if (now - lastTime < CODE_INTERVAL) {
                    return Result.error("验证码发送失败，请稍后再试");
                }
            }
            SimpleMailMessage message = new SimpleMailMessage();
            String code = codeGenerator.generate(6);
            log.info("接收邮箱为{}生成验证码为{}",phone,code);
            message.setSubject("验证码");
            message.setText("你收到的验证码是：" + code + "\n验证码一次有效，五分钟后失效。\n发送时间："
                    + LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
            message.setTo(phone); //收件人邮箱地址,请自行修改
            message.setFrom(username);
            mailSender.send(message);
            //验证码保存到session
            session.setAttribute(phone,code);
            session.setAttribute(phone + "_time", String.valueOf(now));
            log.info("邮箱验证码发送成功，请及时查看");
            return Result.success("邮箱验证码发送成功，请及时查看");
        }
        return Result.error("验证码发送失败，请再次尝试");
    }

    @PostMapping("/login")
    private Result<String> login(@RequestBody Map map, HttpSession session) throws Exception{
        log.info(map.toString());
        //获取邮箱
        String phone = map.get("phone").toString();
        //获取验证码
        String code = map.get("code").toString();
        //从session中获取保存的验证码，对比
        Object sessionCode =  session.getAttribute(phone);
        if (StringUtils.isNotEmpty(code)&&sessionCode.equals(code)) {
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone,phone);
            String sessionTimeStr = (String) session.getAttribute(phone + "_time");
            if (sessionCode != null && sessionTimeStr != null) {
                long sessionTime = Long.parseLong(sessionTimeStr);
                if (System.currentTimeMillis() - sessionTime < CODE_EXPIRE) {
                    if (code.equals(sessionCode)) {
                        //验证通过
                        session.removeAttribute(phone);
                        session.removeAttribute(phone + "_time");
                        return Result.success("验证码验证通过");
                    } else {
                        return Result.error("验证码错误，请重新输入");
                    }
                } else {
                    return Result.error("验证码已失效，请重新获取");
                }
            }
        }
        return Result.error("验证码验证失败，请重新尝试");
    }
}

