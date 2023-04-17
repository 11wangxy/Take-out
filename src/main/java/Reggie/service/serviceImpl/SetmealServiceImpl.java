package Reggie.service.serviceImpl;

import Reggie.mapper.SetMealMapper;
import Reggie.pojo.Setmeal;
import Reggie.service.SetmealService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetMealMapper, Setmeal> implements SetmealService {
}
