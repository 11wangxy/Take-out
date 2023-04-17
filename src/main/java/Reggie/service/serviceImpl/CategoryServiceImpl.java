package Reggie.service.serviceImpl;

import Reggie.EXCEPTION.CustomException;
import Reggie.mapper.CategoryMapper;
import Reggie.pojo.Category;
import Reggie.pojo.Dish;
import Reggie.pojo.Setmeal;
import Reggie.service.CategoryService;
import Reggie.service.DishService;
import Reggie.service.SetmealService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
   //根据id进行删除，查看是否关联相应的套餐
    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;
    @Override
    public void remove(Long id) {
        //是否关联菜品
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(Dish::getCategoryId,id);
        long count = dishService.count(dishLambdaQueryWrapper);
        if (count>0){
            //已经关联业务，抛出异常
            throw new CustomException("当前分类下关联了菜品，无法进行删除");
        }
        //是否关联套餐
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId,id);
        long count1 = setmealService.count(setmealLambdaQueryWrapper);
        if (count1>0){
            //已经关联业务，抛出异常
            throw new CustomException("当前分类下关联了套餐，无法进行删除");
        }
        super.removeById(id);
    }
}
