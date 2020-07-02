package com.lsc.bootstore.controller.backend;

import com.lsc.bootstore.dao.CategoryDao;
import com.lsc.bootstore.entity.Category;
import com.lsc.bootstore.util.ConstraintViolationExceptionHandler;
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

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/manage/category")
public class CategoryController {
    @Autowired
    CategoryDao categoryDao;

    @GetMapping
    public ModelAndView list(@RequestParam(value="async",required=false) boolean async,
                             @RequestParam(value="pageIndex",required=false,defaultValue="0") int pageIndex,
                             @RequestParam(value="pageSize",required=false,defaultValue="10") int pageSize,
                             @RequestParam(value="name",required=false,defaultValue="") String name,
                             Model model) {
        Pageable pageable = PageRequest.of(pageIndex, pageSize);
        name = '%'+name+'%';
        Page<Category> page = categoryDao.findByCnameLike(name,pageable);
        List<Category> list = page.getContent();	// 当前所在页面数据列表
        model.addAttribute("page", page);
        model.addAttribute("List", list);
        return new ModelAndView(async==true?"category/list :: #mainContainerRepleace":"category/list", "categoryModel", model);
    }

    //获取 创建 表单页面
    @GetMapping("/add")
    public ModelAndView createForm(Model model) {
        System.out.println("获取创建表单页面");
        model.addAttribute("category", new Category());
        return new ModelAndView("category/add", "categoryModel", model);
    }

    /**
     * 新建或保存书本
     * @param book
     * @return
     */
    @PostMapping
    public ResponseEntity<Response> saveOrUpdateUser(Category category) {
        System.out.println("进入新建或保存分类的方法");
        try {
            categoryDao.save(category);
        }  catch (ConstraintViolationException e)  {
            return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionHandler.getMessage(e)));
        }
        return ResponseEntity.ok().body(new Response(true, "处理成功", category));
    }

    /**
     * 删除用户
     * @param id
     * @return
     */
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Response> delete(@PathVariable("id") Integer id, Model model) {
        System.out.println("进入删除图书分类的方法");
        try {
            categoryDao.deleteById(id);
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
    public ModelAndView modifyForm(@PathVariable("id") Integer id, Model model) {
        System.out.println("进入修改图书分类方法");
        Optional<Category> category= categoryDao.findById(id);
        model.addAttribute("category", category.get());
        return new ModelAndView("category/add", "categoryModel", model);
    }

}
