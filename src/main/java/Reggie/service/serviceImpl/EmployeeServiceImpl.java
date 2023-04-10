package Reggie.service.serviceImpl;

import Reggie.mapper.EmployeeMapper;
import Reggie.pojo.Employee;
import Reggie.service.EmployeeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}
