package com.lsc.bootstore.service;

import com.lsc.bootstore.dao.OrdersDao;
import com.lsc.bootstore.entity.Book;
import com.lsc.bootstore.entity.CartItem;
import com.lsc.bootstore.entity.Orders;
import com.lsc.bootstore.entity.User;
import com.lsc.bootstore.util.GeneratIDTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class OrderService {
    @Autowired
    OrdersDao ordersDao;
    @Autowired
    BookService bookService;
    @Autowired
    CartService cartService;
    @Autowired
    GeneratIDTool generatIDTool;

    public Page<Orders> getAllOrderList(Pageable pageable){
        return ordersDao.findAllByOrderByCreatedTimeDesc(pageable);
    }

    public Page<Orders> getOrdersByUserId(Long userId,Pageable pageable){
        return ordersDao.findAllByUserIdOrderByCreatedTimeDesc(userId,pageable);
    }

    public Page<Orders> getOrdersByNoLike(String orderNo,Pageable pageable){
        return ordersDao.findByOrderNoContains(orderNo,pageable);
    }

    public void save(Orders orders){
        ordersDao.save(orders);
    }

    public Orders getOrderByNo(String orderNo){
        return  ordersDao.findByOrderNo(orderNo);
    }

    public void deleteOrderById(String orderNo){
        ordersDao.deleteById(orderNo);
    }

    public Orders submitOrder(Orders order,int cartId){
        SecurityContext ctx = SecurityContextHolder.getContext();
        Authentication auth = ctx.getAuthentication();
        User user = (User) auth.getPrincipal();
        //生成订单号
        order.setOrderNo(generatIDTool.getLocalTrmSeqNum());
//        order.setOrderNo("1841513134541");
        order.setUserId(user.getId());
        order.setCreatedTime(new Date());
        order.setUpdatedTime(new Date());
        order.setStatus("未付款");
        for (int i =0;i<order.getOrderDetailList().size();i++){
            order.getOrderDetailList().get(i).setCreatedTime(new Date());
            order.getOrderDetailList().get(i).setUpdatedTime(new Date());
            //设置订单分类
            order.getOrderDetailList().get(i).setProductCategory(bookService.getBookDetail( order.getOrderDetailList().get(i).getProductId()).getCategoryId());
            //销量加一
            bookService.addSalesVolume(order.getOrderDetailList().get(i).getProductId());
        }
        ordersDao.save(order);
        //删除对应的购物车
        if (cartId != -1){
            cartService.deleteCart(cartId);
        }
        return order;
    }

    public List<CartItem> buyNow(Long bid,int quantity){
        //获取用户信息
        SecurityContext ctx = SecurityContextHolder.getContext();
        Authentication auth = ctx.getAuthentication();
        User user = (User) auth.getPrincipal();
        Book book = bookService.getBookDetail(bid);
        List<CartItem> carts = new ArrayList<>();
        CartItem cartItem = new CartItem();
        cartItem.setCart_id(-1);
        cartItem.setB_id(bid);
        cartItem.setUser_id(user.getId());
        cartItem.setBname(book.getBname());
        cartItem.setImg_url(book.getImage_w());
        cartItem.setPrice(book.getPrice());
        cartItem.setQuantity(quantity);
        double sum = (Math.round((book.getPrice()*100)*quantity))/100.0;
        cartItem.setSum(sum);
        carts.add(cartItem);
        return carts;
    }

    public int updateStatus(String orderNo){
        return ordersDao.updateStatus(orderNo);
    }

}
