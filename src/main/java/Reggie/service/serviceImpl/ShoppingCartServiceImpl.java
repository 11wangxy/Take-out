package Reggie.service.serviceImpl;

import Reggie.mapper.ShoppingCartMapper;
import Reggie.pojo.ShoppingCart;
import Reggie.service.ShoppingCartService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.stereotype.Service;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {

}
