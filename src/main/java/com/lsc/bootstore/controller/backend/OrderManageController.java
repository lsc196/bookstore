package com.lsc.bootstore.controller.backend;
import com.lsc.bootstore.dao.OrderDetailDao;
import com.lsc.bootstore.dao.OrdersDao;
import com.lsc.bootstore.entity.Book;
import com.lsc.bootstore.entity.OrderDetail;
import com.lsc.bootstore.entity.Orders;
import com.lsc.bootstore.service.OrderService;
import com.lsc.bootstore.util.GeneratIDTool;
import com.lsc.bootstore.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/manage/order")
public class OrderManageController {
    @Autowired
    OrderService orderService;




    @PostMapping("/updateOrder")
    @ResponseBody
    public void updateOrder(Orders orders){
        orderService.save(orders);
    }

    @GetMapping
    public ModelAndView getAllOrders(@RequestParam(value="async",required=false) boolean async,
                                     @RequestParam(value="pageIndex",required=false,defaultValue="0") int pageIndex,
                                     @RequestParam(value="pageSize",required=false,defaultValue="10") int pageSize,
                                     @RequestParam(value="orderNo",required=false,defaultValue="") String orderNo,
                                     Model model){
        Pageable pageable = PageRequest.of(pageIndex, pageSize);
        List<Orders> list = null;
        if (orderNo.equals("")){
            Page<Orders> page= orderService.getAllOrderList(pageable);
            list = page.getContent();	// 当前所在页面数据列表
            model.addAttribute("page", page);
        }else {
            Page<Orders> page = orderService.getOrdersByNoLike(orderNo,pageable);
            list = page.getContent();	// 当前所在页面数据列表
            model.addAttribute("page", page);
        }
        model.addAttribute("orderList", list);

        return new ModelAndView(async==true?"order/list :: #mainContainerRepleace":"order/list", "model", model);

    }

    @DeleteMapping(value = "/{orderNo}")
    public ResponseEntity<Response> delete(@PathVariable("orderNo") String orderNo, Model model) {
        System.out.println("进入删除订单的方法");
        try {
            orderService.deleteOrderById(orderNo);
        } catch (Exception e) {
            return  ResponseEntity.ok().body( new Response(false, e.getMessage()));
        }
        return  ResponseEntity.ok().body( new Response(true, "处理成功"));
    }

}
