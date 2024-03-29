package Reggie.controller;

import Reggie.common.Result;
import Reggie.pojo.User;
import Reggie.service.UserService;
import Reggie.utils.VerificationCodeGenerator;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;

    @Resource
    private VerificationCodeGenerator codeGenerator;

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private JavaMailSenderImpl mailSender;

    @Value("${spring.mail.username}")
    private String username;

    /**
     * 发送验证码
     *
     * @param user    邮箱信息封装
     * @param session 会话
     * @return
     */

    @PostMapping("/sendMsg")
    private Result<String> sendMsg(@RequestBody User user, HttpSession session) {
        String phone = user.getPhone();
        if (StringUtils.isNotEmpty(phone)) {
            SimpleMailMessage message = new SimpleMailMessage();
            String code = codeGenerator.generate(6);
            log.info("接收邮箱为{}  生成验证码为{}", phone, code);
            message.setSubject("验证码");
            message.setText("你收到的验证码是：" + code + "\n验证码一次有效，五分钟后失效。\n发送时间："
                    + LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
            message.setTo(phone); //收件人邮箱地址,请自行修改
            message.setFrom(username);
            mailSender.send(message);
            //验证码保存到redis
            redisTemplate.opsForValue().set(phone, code, 5, TimeUnit.MINUTES);
            log.info("邮箱验证码发送成功，请及时查看");
            return Result.success("邮箱验证码发送成功，请及时查看");
        }
        return Result.error("验证码发送失败，请再次尝试");
    }

    /**
     * 用户登录
     *
     * @param map     用户信息封装
     * @param session 会话
     * @return
     * @throws Exception
     */
    @PostMapping("/login")
    private Result<User> login(@RequestBody Map map, HttpSession session) throws Exception {
        log.info(map.toString());
        //获取邮箱
        String phone = map.get("phone").toString();
        //获取验证码
        String code = map.get("code").toString();
        //从redis中获取保存的验证码，对比
        Object sessionCode = redisTemplate.opsForValue().get(phone);
        if (StringUtils.isNotEmpty(code) && sessionCode != null && sessionCode.equals(code)) {
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone, phone);
            User user = userService.getOne(queryWrapper);

            if (user == null) {
                user = new User();
                user.setPhone(phone);
                user.setStatus(1);
                userService.save(user);
            }
            session.setAttribute("user", user.getId());
            log.info("登录成功");
            redisTemplate.delete(phone);
            return Result.success(user);
        }
        return Result.error("验证失败，请重试");
    }
}

