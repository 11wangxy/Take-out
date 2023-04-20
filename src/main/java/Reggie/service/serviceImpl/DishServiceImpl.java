package Reggie.service.serviceImpl;

import Reggie.DTO.DishDTO;
import Reggie.mapper.DishMapper;
import Reggie.pojo.Dish;
import Reggie.pojo.DishFlavor;
import Reggie.service.DishFlavorService;
import Reggie.service.DishService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private DishFlavorService dishFlavorService;

    /**
     * 新增菜品，同时新增口味数据
     * 首先，将菜品信息保存到菜品表中，然后获取菜品ID。
     * 接着，获取菜品口味列表，使用流式操作将每个口味对象的菜品ID属性设置为当前菜品ID，
     * 然后将修改后的口味对象重新收集到一个新的口味列表中。
     * 最后，将修改后的口味列表保存到菜品口味表中。
     *
     * @param dishDTO
     */
    @Transactional
    public void saveWithFlavor(DishDTO dishDTO) {
        //保存菜品信息到菜品表dish
        this.save(dishDTO);
        Long dishDTOId = dishDTO.getId();
        List<DishFlavor> flavors = dishDTO.getFlavors();
        flavors.stream().map((item) ->
        {
            item.setDishId(dishDTOId);
            return item;
        }).collect(Collectors.toList());
        //保存菜品口味数据到菜品口味表
        dishFlavorService.saveBatch(dishDTO.getFlavors());
    }


}

