package Reggie.service.serviceImpl;

import Reggie.mapper.DishMapper;
import Reggie.pojo.Dish;
import Reggie.service.DishService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
}
