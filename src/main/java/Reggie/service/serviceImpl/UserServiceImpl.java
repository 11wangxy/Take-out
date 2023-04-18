package Reggie.service.serviceImpl;

import Reggie.mapper.UserMapper;
import Reggie.pojo.User;
import Reggie.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
