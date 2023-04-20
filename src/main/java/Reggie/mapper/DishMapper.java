package Reggie.mapper;

import Reggie.DTO.DishPageQueryDTO;
import Reggie.pojo.Dish;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DishMapper extends BaseMapper<Dish> {

}
