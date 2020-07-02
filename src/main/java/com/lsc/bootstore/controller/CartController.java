package com.lsc.bootstore.controller;

import com.lsc.bootstore.dao.CartDao;
import com.lsc.bootstore.entity.CartItem;
import com.lsc.bootstore.entity.Orders;
import com.lsc.bootstore.entity.User;
import com.lsc.bootstore.service.BookService;
import com.lsc.bootstore.service.CartService;
import com.lsc.bootstore.service.OrderService;
import com.lsc.bootstore.util.GeneratIDTool;
import com.lsc.bootstore.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class CartController {
    @Autowired
    CartService cartService;


    @GetMapping("/cart")
    public ModelAndView cartlist(Model model){
        //获取用户信息
        SecurityContext ctx = SecurityContextHolder.getContext();
        Authentication auth = ctx.getAuthentication();
        User user = (User) auth.getPrincipal();
        System.out.println("用户："+user.getUsername()+" 进入购物车页面");
        List<CartItem> carts = cartService.getCartListByUserId(user.getId());
        for (CartItem c:carts){
            System.out.println(c);
        }
        model.addAttribute("cartList",carts);
        return new ModelAndView("cart","cartmodel",model);
    }

    @GetMapping("/order")
    public ModelAndView forwordToOrder(String[] cartIds,Model model){
        //获取用户信息
        SecurityContext ctx = SecurityContextHolder.getContext();
        Authentication auth = ctx.getAuthentication();
        User user = (User) auth.getPrincipal();
        List<CartItem> carts = cartService.getCartListByUserId(user.getId());
        List list = new ArrayList<String>(Arrays.asList(cartIds));
        carts = carts.stream().filter(x -> list.contains(String.valueOf(x.getCart_id()))).collect(Collectors.toList());
        double total = 0;
        for (CartItem c :carts){
            total += c.getSum();
        }
        model.addAttribute("cartList",carts);
        model.addAttribute("totalSum",total);
        return new ModelAndView("order","model",model);
    }




    @PostMapping("/cart/add")
    @ResponseBody
    public Response cartAdd(@RequestParam("bid") Long bid, @RequestParam(value = "quantity",defaultValue = "1") int quantity) {
        SecurityContext ctx = SecurityContextHolder.getContext();
        Authentication auth = ctx.getAuthentication();
        User user = (User) auth.getPrincipal();
        cartService.cartAdd(user.getId(), bid, quantity);
        return new Response(true,"添加购物车成功","成功");
    }

    @PostMapping("/cart/quantity/add")
    public ResponseEntity cardQuantityAdd(@RequestParam("cartId") int cartid){
        cartService.QuantityAdd(cartid);
        return  ResponseEntity.ok().body( new Response(true, "处理成功"));
    }

    @PostMapping("/cart/quantity/dec")
    public ResponseEntity cardQuantityDec(@RequestParam("cartId") int cartid){
        cartService.QuantityDec(cartid);
        return  ResponseEntity.ok().body( new Response(true, "处理成功"));
    }

    @PostMapping("/cart/delete")
    public ResponseEntity  cartDelete(@RequestParam("cartId") int cartId){
        cartService.deleteCart(cartId);
        return  ResponseEntity.ok().body( new Response(true, "处理成功"));
    }
}
