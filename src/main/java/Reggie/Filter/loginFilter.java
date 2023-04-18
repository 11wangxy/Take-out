package Reggie.Filter;

import Reggie.common.Result;
import Reggie.utils.BaseContext;
import com.alibaba.fastjson2.JSON;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.Request;
import org.springframework.util.AntPathMatcher;

import java.io.IOException;
import java.net.URI;

@WebFilter(filterName = "loginFilter",urlPatterns = "/*")
@Slf4j
public class loginFilter implements Filter {
    //路径匹配器，支持通配符
    public static final AntPathMatcher PATH_MATCHER=new AntPathMatcher();
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse =(HttpServletResponse) servletResponse;
        //获取请求的URI
        String requestURI = httpServletRequest.getRequestURI();
//        log.info("拦截到请求{}", requestURI);
        //把不需要请求的放在一起
        String[] urls=new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/demo/**",
                "/common/**",
                "/user/sendMsg",
                "/user/login"
        };
        //遍历检查
        boolean check = check(urls, requestURI);
        //如果不需要处理则直接放行
        if (check){
//            log.info("本次请求不需要处理{}",requestURI);
            filterChain.doFilter(httpServletRequest,httpServletResponse);
            return;
        }
        //1判断网页端登录状态，如果登录成功则直接放行.
       if(httpServletRequest.getSession().getAttribute("employee")!=null){
           log.info("用户{}已经登录",httpServletRequest.getSession().getAttribute("employee"));
           Long empl = (Long) httpServletRequest.getSession().getAttribute("employee");
           BaseContext.setID(empl);
           filterChain.doFilter(httpServletRequest,httpServletResponse);
           return;
       }
        //2判断移动端登录状态，如果登录成功则直接放行.
        if(httpServletRequest.getSession().getAttribute("user")!=null){
            log.info("用户{}已经登录",httpServletRequest.getSession().getAttribute("user"));
            Long userid = (Long) httpServletRequest.getSession().getAttribute("user");
            BaseContext.setID(userid);
            filterChain.doFilter(httpServletRequest,httpServletResponse);
            return;
        }
        log.info("用户未登录");
        //未登录则返回未登录结果
        httpServletResponse.getWriter().write(JSON.toJSONString(Result.error("NOTLOGIN")));
        return;
    }

    //检查路径是否符合要求
    public boolean check(String[] urls,String requestURI) {
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, requestURI);
            if (match) {
                return true;
            }
        }
        return false;
    }
}
