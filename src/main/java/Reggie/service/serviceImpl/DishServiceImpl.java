package Reggie.service.serviceImpl;

import Reggie.DTO.DishDTO;
import Reggie.EXCEPTION.CustomException;
import Reggie.mapper.DishMapper;
import Reggie.pojo.Dish;
import Reggie.pojo.DishFlavor;
import Reggie.pojo.Setmeal;
import Reggie.service.DishFlavorService;
import Reggie.service.DishService;
import Reggie.service.SetmealService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private DishFlavorService dishFlavorService;
    @Autowired
    private SetmealService setmealService;
    /**
     * 新增菜品，同时新增口味数据
     * 首先，将菜品信息保存到菜品表中，然后获取菜品ID。
     * 接着，获取菜品口味列表，使用流式操作将每个口味对象的菜品ID属性设置为当前菜品ID，
     * 然后将修改后的口味对象重新收集到一个新的口味列表中。
     * 最后，将修改后的口味列表保存到菜品口味表中。
     * INSERT INTO dish (name, price, description, created_time, updated_time) VALUES (dishDTO.name, dishDTO.price, dishDTO.description, now(), now());
     * SELECT LAST_INSERT_ID() AS id;
     * INSERT INTO dish_flavor (flavor_name, dish_id) VALUES (flavors.flavor_name, dishDTOId);
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

    /**
     * 根据菜品查询口味信息
     * @param id
     * @return
     */
    @Override
    public DishDTO getByIdwithFlavors(Long id) {
        Dish dish = this.getById(id);
        DishDTO dishDTO = new DishDTO();
        BeanUtils.copyProperties(dish,dishDTO);
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,dish.getId());
        List<DishFlavor> dishFlavors = dishFlavorService.list(queryWrapper);
        dishDTO.setFlavors(dishFlavors);
        return dishDTO;
    }

    /**
     * 更新菜品信息
     * @param dishiDTO
     */
    @Override
    @Transactional
    public void updateWithFlavor(DishDTO dishiDTO) {
        //更新dish表中基本信息
        LocalDateTime updateTime = LocalDateTime.now(); //添加默认值
        dishiDTO.setUpdateTime(updateTime); //给更新时间赋上默认值
        this.updateById(dishiDTO);
        //删除当前菜品对应的口味数据
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,dishiDTO.getId());
        dishFlavorService.remove(queryWrapper);
        //添加口味数据
        List<DishFlavor> flavors = dishiDTO.getFlavors();
        flavors.stream().map((item) ->
        {
            item.setDishId(dishiDTO.getId());
            item.setUpdateTime(updateTime); //给口味更新时间赋上默认值
            return item;
        }).collect(Collectors.toList());

        dishFlavorService.saveBatch(flavors);
    }
    /**
     * 删除套菜，同时删除与菜品的关联信息
     * select(*) from setmeal where id in (?,?,?) and status=1
     * @param ids
     */
//    @Override
//    public void removeWithDish(List<Long> ids) {
//
//        //查询套餐信息，确定是否可以删除
//        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
//
//        queryWrapper.in(Setmeal::getId,ids);
//        queryWrapper.eq(Setmeal::getStatus,1);
//
//        int count = super.count(queryWrapper);
//        if (count>0){//如果不能删除，则抛出一个业务异常的
//            throw new CustomException("套餐正在售卖中，无法删除");
//        }
//
//        //如果可以删除，先删除套餐中的数据
//        super.removeByIds(ids);
//        //删除关系表中的数据
//        setmealService.removeByIds(ids);
//    }
}

