package com.lsc.bootstore.controller;

import com.lsc.bootstore.entity.CartItem;
import com.lsc.bootstore.entity.Orders;
import com.lsc.bootstore.entity.User;
import com.lsc.bootstore.service.OrderService;
import com.lsc.bootstore.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
public class OrderController {

    @Autowired
    OrderService orderService;

    //cartId 只是用来判断是否点立即购买过来的  -1表示立即购买过来的
    @PostMapping("/submitOrder")
    public String submitOrder(Orders order,int cartId, RedirectAttributes attributes){
        System.out.println(order);
        Orders result = orderService.submitOrder(order,cartId);
        attributes.addFlashAttribute("order",result);
        return "redirect:/payment/pay";
    }

    @GetMapping("/myOrders")
    public ModelAndView myOrders(@RequestParam(value="pageIndex",required=false,defaultValue="0") int pageIndex,
                                 @RequestParam(value="pageSize",required=false,defaultValue="100") int pageSize,
                                 Model model){
        //获取用户信息
        SecurityContext ctx = SecurityContextHolder.getContext();
        Authentication auth = ctx.getAuthentication();
        User user = (User) auth.getPrincipal();
        Pageable pageable = PageRequest.of(pageIndex, pageSize);
        Page<Orders> myOrders = orderService.getOrdersByUserId(user.getId(),pageable);
        model.addAttribute("myOrders",myOrders);
        return new ModelAndView("myOrder","model",model);
    }

    @DeleteMapping(value = "/deleteOrder/{orderNo}")
    public ResponseEntity<Response> delete(@PathVariable("orderNo") String orderNo, Model model) {
        System.out.println("进入删除用户订单的方法");
        SecurityContext ctx = SecurityContextHolder.getContext();
        Authentication auth = ctx.getAuthentication();
        User user = (User) auth.getPrincipal();
        try {
            orderService.deleteOrderById(orderNo);
        } catch (Exception e) {
            return  ResponseEntity.ok().body( new Response(false, e.getMessage()));
        }
        return  ResponseEntity.ok().body( new Response(true, "处理成功"));
    }

    @GetMapping("/buyNow")
    public ModelAndView buyNow(Long bid,int quantity,Model model){
        List<CartItem> carts = orderService.buyNow(bid,quantity);
        model.addAttribute("cartList",carts);
        model.addAttribute("totalSum",carts.get(0).getSum());
        return new ModelAndView("buyNow","model",model);
    }
}
