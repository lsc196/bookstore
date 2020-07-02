package com.lsc.bootstore.controller.backend;

import com.lsc.bootstore.BootstoreApplication;
import com.lsc.bootstore.dao.BookDao;
import com.lsc.bootstore.dao.OrderDetailDao;
import com.lsc.bootstore.dao.OrdersDao;
import com.lsc.bootstore.entity.Book;
import com.lsc.bootstore.entity.OrderDetail;
import com.lsc.bootstore.entity.Orders;
import com.lsc.bootstore.entity.User;
import com.lsc.bootstore.service.OrderService;
import com.lsc.bootstore.util.GeneratIDTool;
import com.lsc.bootstore.util.RedisUtil;
import com.lsc.bootstore.vo.SalasVo;
import com.lsc.bootstore.vo.ZheXianVo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {BootstoreApplication.class})
class OrderManageControllerTest {
    @Autowired
    OrderService orderService;
    @Autowired
    OrderDetailDao orderDetailDao;
    @Autowired
    OrdersDao ordersDao;
    @Autowired
    BookDao bookDao;
    @Resource
    private RedisUtil redisUtil;

    @Test
    void saveOrder() {

        List<Object[]> list = orderDetailDao.findDaySalas();
        for (Object[] objs : list) {
            for (int i = 0; i < objs.length; i++) {
                System.out.print(String.valueOf(objs[i]) + ",");
            }
            System.out.println();
        }
//
//        List<ZheXianVo> zheXianVos = orderDetailDao.findDaySalas();
//        for (ZheXianVo zheXianVo:zheXianVos){
//            System.out.println(zheXianVo);
//        }


    }


}