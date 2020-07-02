package com.lsc.bootstore.controller.backend;


import com.lsc.bootstore.vo.Menu;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

/**
 * 后台管理控制器
 */
@Controller
@RequestMapping("/admins")
public class AdminController {

    /**
     * 获取后台管理主页面
     * @return
     */
    @GetMapping
    public ModelAndView listUsers(Model model) {
        List<Menu> list = new ArrayList<>();
        list.add(new Menu("用户管理", "/manage/user"));
        list.add(new Menu("分类管理", "/manage/category"));
        list.add(new Menu("图书管理", "/manage/book"));
        list.add(new Menu("订单管理", "/manage/order"));
        list.add(new Menu("统计管理", "/manage/statistics"));
        model.addAttribute("list", list);
        return new ModelAndView("/admins/index", "model", model);
    }

}