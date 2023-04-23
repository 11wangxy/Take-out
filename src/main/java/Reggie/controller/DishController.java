package Reggie.controller;

import Reggie.DTO.DishDTO;
import Reggie.common.Result;
import Reggie.pojo.Category;
import Reggie.pojo.Dish;
import Reggie.pojo.DishFlavor;
import Reggie.service.CategoryService;
import Reggie.service.DishFlavorService;
import Reggie.service.DishService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/dish")
public class DishController {
    @Autowired
    private DishService dishService;
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private DishFlavorService dishFlavorService;

    /**
     * 新增菜品
     * @param dishiDTO
     * @return
     */
    @PostMapping
    //@RB用于指定一个HTTP请求的请求体（body）应该被映射到一个Java对象上。通常用于处理POST请求。
    //可以将请求体中的JSON对象自动转换为Java中的对应对象
    public Result<String> save(@RequestBody DishDTO dishiDTO){
        dishService.saveWithFlavor(dishiDTO);
        return Result.success("新增菜品成功");
    }
    @GetMapping("/page")
    public Result<Page> page(Integer page,Integer pageSize,String name){
        Page<Dish> pageInfo = new Page<>(page, pageSize);
        Page<DishDTO> dishDTOPage = new Page<>();
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(name!=null,Dish::getName,name);
        queryWrapper.orderByDesc(Dish::getUpdateTime);
        dishService.page(pageInfo,queryWrapper);

        BeanUtils.copyProperties(pageInfo,dishDTOPage,"records");

        List<Dish> records = pageInfo.getRecords();

        List<DishDTO> list = records.stream().map((item) -> {
            DishDTO dishDTO = new DishDTO();
            BeanUtils.copyProperties(item, dishDTO);
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            String categoryName = category.getName();
            dishDTO.setCategoryName(categoryName);
            return dishDTO;
        }).collect(Collectors.toList());

        dishDTOPage.setRecords(list);
        
        return Result.success(dishDTOPage);
    }

    /**
     * @PathVariable是Spring MVC中的一个注解，用于从请求的URL中提取变量的值，
     * 并将其作为方法参数传递给控制器方法。例如，如果URL为“/users/{id}”，
     * 则@PathVariable("id")可以将该URL中的“id”值提取出来，
     * 并将其作为方法参数传递给控制器方法。这个注解通常用于RESTful服务中，
     * 以便从请求中获取资源的唯一标识符。
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<DishDTO> get(@PathVariable Long id){
        DishDTO byIdwithFlavors = dishService.getByIdwithFlavors(id);
        return Result.success(byIdwithFlavors);

    }

    @PutMapping
    //@RB用于指定一个HTTP请求的请求体（body）应该被映射到一个Java对象上。通常用于处理POST请求。
    //可以将请求体中的JSON对象自动转换为Java中的对应对象
    public Result<String> update(@RequestBody DishDTO dishiDTO){
        dishService.updateWithFlavor(dishiDTO);
        return Result.success("新增菜品成功");
    }


    /**
     * 根据条件查询菜品数据
     * @param dish
     * @return
     */
    @GetMapping("/list")
    public Result<List<Dish>> list(Dish dish){
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId()!=null,Dish::getCategoryId, dish.getCategoryId());
        //查询状态为1，起售的菜品
        LambdaQueryWrapper<Dish> status = queryWrapper.eq(Dish::getStatus, 1);
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        List<Dish> list = dishService.list(queryWrapper);
        return Result.success(list);
    }
}
