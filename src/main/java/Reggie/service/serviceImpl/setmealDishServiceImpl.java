package Reggie.service.serviceImpl;

import Reggie.DTO.SetmealDto;
import Reggie.mapper.setmealDishMapper;
import Reggie.pojo.SetmealDish;
import Reggie.service.setmealDishService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import Reggie.DTO.SetmealDto;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class setmealDishServiceImpl extends ServiceImpl<setmealDishMapper,SetmealDish> implements setmealDishService {
}