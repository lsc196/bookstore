package com.lsc.bootstore.controller.backend;

import com.lsc.bootstore.entity.SysRole;
import com.lsc.bootstore.entity.User;
import com.lsc.bootstore.service.AuthorityService;
import com.lsc.bootstore.service.UserService;
import com.lsc.bootstore.util.ConstraintViolationExceptionHandler;
import com.lsc.bootstore.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/manage/user")
public class UserManageController {

    @Autowired
    private UserService userService;

    @Autowired
    AuthorityService authorityService;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    //查询所有用户
    @GetMapping
    public ModelAndView list(@RequestParam(value="async",required=false) boolean async,
                             @RequestParam(value="pageIndex",required=false,defaultValue="0") int pageIndex,
                             @RequestParam(value="pageSize",required=false,defaultValue="10") int pageSize,
                             @RequestParam(value="name",required=false,defaultValue="") String name,
                             Model model) {

        Pageable pageable = PageRequest.of(pageIndex, pageSize);
        Page<User> page = userService.listUsersByNameLike(name, pageable);
        List<User> list = page.getContent();	// 当前所在页面数据列表
        model.addAttribute("page", page);
        model.addAttribute("userList", list);
        return new ModelAndView(async==true?"users/list :: #mainContainerRepleace":"users/list", "userModel", model);
    }

    //获取 创建 表单页面
    @GetMapping("/add")
    public ModelAndView createForm(Model model) {
        System.out.println("获取创建表单页面");
        model.addAttribute("user", new User(null, null, null, null));
        return new ModelAndView("users/add", "userModel", model);
    }

    /**
     * 新建或保存用户
     * @param user
     * @return
     */
    @PostMapping
    public ResponseEntity<Response> saveOrUpdateUser(User user, Long authorityId) {
        System.out.println("进入新建或保存用户的方法");
        List<SysRole> authorities = new ArrayList<SysRole>();
        authorities.add(authorityService.getRoleById(authorityId));
        user.setRoles(authorities);

        //如果为新增用户且密码为空字符串
        if(user.getId() == null && user.getPassword() == ""){
            //不对密码进行加密处理（否则加密后密码变为不空字符串，不会抛出异常，仍然添加成功）
        }else if(user.getId() == null) { //如果为新增用户且密码不为空字符串，做加密处理
            user.setPassword(passwordEncoder.encode(user.getPassword())); // 加密密码
        }else{ //修改用户，判断密码有没有修改
            String oldPwd = userService.getUserById(user.getId()).getPassword();
            if(!user.getPassword().equals(oldPwd)){
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            }
        }
        try {
            userService.saveOrUpdateUser(user);
        }  catch (ConstraintViolationException e)  {
            return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionHandler.getMessage(e)));
        }

        System.out.println("进入saveOrUpdateUser方法");
        return ResponseEntity.ok().body(new Response(true, "处理成功", user));
    }

    /**
     * 删除用户
     * @param id
     * @return
     */
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Response> delete(@PathVariable("id") Long id, Model model) {
        System.out.println("进入删除用户的方法");
        try {
            userService.removeUser(id);
        } catch (Exception e) {
            return  ResponseEntity.ok().body( new Response(false, e.getMessage()));
        }
        return  ResponseEntity.ok().body( new Response(true, "处理成功"));
    }

    /**
     * 获取修改用户的界面，及数据
     * @param id
     * @return
     */
    @GetMapping(value = "edit/{id}")
    public ModelAndView modifyForm(@PathVariable("id") Long id, Model model) {
        System.out.println("进入修改用户方法z");
        User user = userService.getUserById(id);
        model.addAttribute("user", user);
        return new ModelAndView("users/edit", "userModel", model);
    }


}
