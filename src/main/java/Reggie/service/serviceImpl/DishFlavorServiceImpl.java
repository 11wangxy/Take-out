package Reggie.service.serviceImpl;

import Reggie.mapper.DishFlavorMapper;
import Reggie.pojo.DishFlavor;
import Reggie.service.DishFlavorService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.awt.datatransfer.DataFlavor;
@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService{
}