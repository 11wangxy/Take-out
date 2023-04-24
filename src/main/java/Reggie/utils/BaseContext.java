package Reggie.utils;

import org.springframework.stereotype.Component;

/*获取threadlocal工具类*/
@Component
public class BaseContext {
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();
    public static void setID(Long id){
        threadLocal.set(id);
    }
    public static Long getId(){
        return threadLocal.get();
    }
}
