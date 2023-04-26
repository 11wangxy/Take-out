package Reggie;


import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Slf4j
public class testRedis2 {
    @Resource
    private RedisTemplate redisTemplate;
    @Test
    @Transactional
    public void test(){
        redisTemplate.opsForValue().set("age","15");
        Object name = redisTemplate.opsForValue().get("name");
        System.out.println(name);
    }
}
