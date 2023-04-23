package Reggie.service.serviceImpl;

import Reggie.DTO.SetmealDto;
import Reggie.EXCEPTION.CustomException;
import Reggie.mapper.SetMealMapper;
import Reggie.pojo.Setmeal;
import Reggie.pojo.SetmealDish;
import Reggie.service.SetmealService;
import Reggie.service.setmealDishService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetMealMapper, Setmeal> implements SetmealService {
    @Autowired
    private Reggie.service.setmealDishService setmealDishService;
    /**
     * 新增套餐，同时关联菜品信息
     * @param setmealDto
     */

    @Transactional
    public void saveWithDish(SetmealDto setmealDto) {
        this.save(setmealDto);
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes.stream().map((item)->{
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());
        setmealDishService.saveBatch(setmealDishes);
    }

    /**
     * 删除套菜，同时删除与菜品的关联信息
     * select(*) from setmeal where id in (?,?,?) and status=1
     * @param ids
     */
    @Override
    @Transactional
    public void removeWithDish(List<Long> ids) {

        //查询套餐信息，确定是否可以删除
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.in(Setmeal::getId,ids);
        queryWrapper.eq(Setmeal::getStatus,1);

        long count = super.count(queryWrapper);
        if (count>0){//如果不能删除，则抛出一个业务异常的
            throw new CustomException("套餐正在售卖中，无法删除");
        }

        //如果可以删除，先删除套餐中的数据
        this.removeByIds(ids);

        LambdaQueryWrapper<SetmealDish> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.in(SetmealDish::getSetmealId,ids);
        //删除关系表中的数据
        setmealDishService.remove(queryWrapper1);
    }
}
