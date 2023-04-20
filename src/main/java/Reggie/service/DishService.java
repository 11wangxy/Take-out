package Reggie.service;

import Reggie.DTO.DishDTO;
import Reggie.pojo.Dish;
import com.baomidou.mybatisplus.extension.service.IService;

public interface DishService extends IService<Dish> {

    void saveWithFlavor(DishDTO dishiDTO);


}
