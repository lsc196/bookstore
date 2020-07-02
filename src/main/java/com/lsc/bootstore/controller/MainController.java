package com.lsc.bootstore.controller;

import com.lsc.bootstore.entity.Book;
import com.lsc.bootstore.entity.SysRole;
import com.lsc.bootstore.entity.User;
import com.lsc.bootstore.service.AuthorityService;
import com.lsc.bootstore.service.BookService;
import com.lsc.bootstore.service.UserService;
import com.lsc.bootstore.util.ConstraintViolationExceptionHandler;
import com.lsc.bootstore.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class MainController {

    private static final Long ROLE_USER_AUTHORITY_ID = 2L;

    @Autowired
    UserService userService;
    @Autowired
    AuthorityService authorityService;
    @Autowired
    BookService bookService;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    //首次请求页面跳转到首页
    @RequestMapping("/")
    public String root(Model model){
        System.out.println("进入网上书店首页");
        return "redirect:index";
    }

    @GetMapping("/index")
    public String index(Model model){
        Pageable pageable = PageRequest.of(0, 10);
        Page<Book> recommendBooks = bookService.recommendBooks(pageable);
        Page<Book> hotBooks = bookService.hotBooks(pageable);
        model.addAttribute("searchName",null);
        model.addAttribute("hotBooks",hotBooks);
        model.addAttribute("recommendBooks",recommendBooks);
        return "index";
    }

    //跳转到登陆页面
    @GetMapping("/login")
    public String login(){
        return "login";
    }

    //跳转到登陆页面，并显示错误信息
    @GetMapping("/login-error")
    public String loginError(Model model){
        model.addAttribute("loginError", true);
        model.addAttribute("errorMsg", "登陆失败，用户名或者密码错误！");
        return "login";
    }

    @GetMapping("/register")
    public ModelAndView redictToRegister(){
        return new ModelAndView("register");
    }

    @PostMapping("/register")
    public String registerUser(User user,Model model){
        if(userService.getUserByUsername(user.getUsername())!=null){
            model.addAttribute("registerError", true);
            model.addAttribute("errorMsg", "用户已经存在，请更换用户！");
            return "register";
        }
        if(userService.getUserByEmail(user.getEmail())!=null){
            model.addAttribute("registerError", true);
            model.addAttribute("errorMsg", "邮箱已存在,请更换邮箱！");
            return "register";
        }
        user.setPassword(passwordEncoder.encode(user.getPassword())); // 加密密码

        List<SysRole> authorities = new ArrayList<>();
        //只要是注册的均为普通用户
        authorities.add(authorityService.getRoleById(ROLE_USER_AUTHORITY_ID));
        user.setRoles(authorities);
        userService.registerUser(user);
        return "redirect:/login";
    }

    @GetMapping("/userCenter")
    public ModelAndView userCenter(Model model){
        //获取用户信息
        SecurityContext ctx = SecurityContextHolder.getContext();
        Authentication auth = ctx.getAuthentication();
        User user = (User) auth.getPrincipal();
        model.addAttribute("user",user);
        return new ModelAndView("userCenter","model",model);
    }

    /**
     * 新建或保存用户
     * @param user
     * @return
     */
    @PostMapping("/updateUesr")
    public String saveOrUpdateUser(User user,Model model) {
        System.out.println("进入用户中心更新用户的方法");
        System.out.println("进入删除用户订单的方法");
        SecurityContext ctx = SecurityContextHolder.getContext();
        Authentication auth = ctx.getAuthentication();
        User oldUser = (User) auth.getPrincipal();
        if(!user.getPassword().equals(oldUser.getPassword())){
            oldUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        if (!user.getEmail().equals(oldUser.getEmail())&&userService.getUserByEmail(user.getEmail()) != null){
            model.addAttribute("upodateError", true);
            model.addAttribute("errorMsg", "邮箱已经存在，请更换邮箱！");
            return "userCenter";
        }
        try {
            oldUser.setPhone(user.getPhone());
            oldUser.setEmail(user.getEmail());
            userService.saveOrUpdateUser(oldUser);
        }  catch (ConstraintViolationException e)  {
            return "index";
        }
        return "index";
    }


}
