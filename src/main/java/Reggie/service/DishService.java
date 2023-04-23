package Reggie.service;

import Reggie.DTO.DishDTO;
import Reggie.pojo.Dish;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface DishService extends IService<Dish> {

    void saveWithFlavor(DishDTO dishiDTO);

    public DishDTO getByIdwithFlavors(Long id);

    void updateWithFlavor(DishDTO dishiDTO);
}
