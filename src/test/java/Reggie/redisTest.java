package Reggie;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import redis.clients.jedis.Jedis;

public class redisTest {
    private Jedis jedis;
    @BeforeEach
    void setup(){

        jedis = new Jedis("192.168.111.100",6379);
        jedis.auth("20030111");
        jedis.select(0);
    }

    @Test
    void test(){
        jedis.set("name","petter");
        String name = jedis.get("name");
        System.out.println(name);
    }
    @AfterEach
    void down(){
        if (jedis!=null){
            jedis.close();
        }
    }
}
