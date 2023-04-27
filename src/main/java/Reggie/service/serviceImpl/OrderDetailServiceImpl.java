package Reggie.service.serviceImpl;

import Reggie.mapper.OrderDetailMapper;
import Reggie.pojo.OrderDetail;
import Reggie.service.OrderDetailService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {

}