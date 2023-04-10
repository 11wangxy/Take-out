package Reggie.controller;

import Reggie.common.Result;
import Reggie.pojo.Employee;
import Reggie.service.EmployeeService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/login")
    public Result<Employee> login(HttpServletRequest request, @RequestBody Employee employee){
        //md5加密
        String password = employee.getPassword();
        String passwordMd5 = DigestUtils.md5DigestAsHex(password.getBytes());
        //根据用户民查询对象
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername,employee.getUsername());
        Employee emp = (Employee) employeeService.getOne(queryWrapper);
        if(emp==null){
            return Result.error("查询失败");
        }
        if(!emp.getPassword().equals(passwordMd5)){
            return Result.error("查询失败");
        }
        if (emp.getStatus()==0)
            return Result.error("该账号失效");

        request.getSession().setAttribute("employee",emp.getId());
        return Result.success(emp);

    }
    @PostMapping("/logout")
    private Result<String> logout(HttpServletRequest request){
        //清理session中保存的id
        request.getSession().removeAttribute("employee");
        return Result.success("退出成功");
    }
    @PostMapping
    private Result<String> save(HttpServletRequest request,@RequestBody Employee employee){
        log.info("新增员工{}",employee.toString());
        //设置初始密码
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());
        Long employeeId = (Long) request.getSession().getAttribute("employee");
        employee.setCreateUser(employeeId);
        employee.setUpdateUser(employeeId);
        employeeService.save(employee);
        return Result.success("新增员工成功");
    }

    @GetMapping("/page")
    private Result<com.baomidou.mybatisplus.extension.plugins.pagination.Page> selectAllByPage(Optional<Integer> page, Optional<Integer> size, Optional<String> name){
        Integer pageNum = page.orElse(1);
        Integer pageSize = size.orElse(10);
        String searchName = name.orElse("");
        log.info("page={},pageSize={},name={}",pageNum,pageSize,searchName);
        //分页构造
        com.baomidou.mybatisplus.extension.plugins.pagination.Page pageInfo = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(pageNum, pageSize);
        //构造条件解析器
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.like(StringUtils.isNotEmpty(searchName),Employee::getName, searchName);//过滤条件
        //排序
        queryWrapper.orderByDesc(Employee::getUpdateTime);
        //执行查询
        employeeService.page(pageInfo,queryWrapper);
        return Result.success(pageInfo);
    }

}
