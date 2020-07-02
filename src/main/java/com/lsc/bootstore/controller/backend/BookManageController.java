package com.lsc.bootstore.controller.backend;
import com.lsc.bootstore.dao.CategoryDao;
import com.lsc.bootstore.entity.Book;
import com.lsc.bootstore.entity.Category;
import com.lsc.bootstore.service.BookService;
import com.lsc.bootstore.util.ConstraintViolationExceptionHandler;
import com.lsc.bootstore.util.FileUploadService;
import com.lsc.bootstore.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.ConstraintViolationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/manage/book")
public class BookManageController {

    @Autowired
    private BookService bookService;
    @Autowired
    CategoryDao categoryDao;

    //查询所有用户
    @GetMapping
    public ModelAndView list(@RequestParam(value="async",required=false) boolean async,
                             @RequestParam(value="pageIndex",required=false,defaultValue="0") int pageIndex,
                             @RequestParam(value="pageSize",required=false,defaultValue="10") int pageSize,
                             @RequestParam(value="name",required=false,defaultValue="") String name,
                             Model model) {

        Pageable pageable = PageRequest.of(pageIndex, pageSize);
        Page<Book> page = bookService.listBookByNameLike(name,pageable);
        List<Book> list = page.getContent();	// 当前所在页面数据列表
        model.addAttribute("page", page);
        model.addAttribute("bookList", list);
        return new ModelAndView(async==true?"books/list :: #mainContainerRepleace":"books/list", "bookModel", model);
    }

    //获取 创建 表单页面
    @GetMapping("/add")
    public ModelAndView createForm(Model model) {
        System.out.println("获取创建表单页面");
        List<Category>  cateList= categoryDao.findAll();
        model.addAttribute("book", new Book());
        model.addAttribute("categoryList", cateList);
        return new ModelAndView("books/add", "bookModel", model);
    }

    /**
     * 新建或保存书本
     * @param book
     * @return
     */
    @PostMapping
    public ResponseEntity<Response> saveOrUpdateUser(@RequestParam("file") MultipartFile[] file,Book book) {
        System.out.println("进入新建或保存图书的方法");
        System.out.println("一共上传了"+file.length+"文件");
        //file[0]对应html页面第一个<input type="file" name="file"/>  name的值需要跟@RequestParam的值一致才会进入文件数组
        if(!file[0].isEmpty()){
            book.setImage_w(FileUploadService.fileUpload(file[0]));
        }
        if(!file[1].isEmpty()){
            book.setImage_b(FileUploadService.fileUpload(file[1]));
        }
        try {
            bookService.saveBook(book);
        }  catch (ConstraintViolationException e)  {
            return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionHandler.getMessage(e)));
        }
        System.out.println("进入saveBook方法");
        return ResponseEntity.ok().body(new Response(true, "处理成功", book));
    }

    /**
     * 删除用户
     * @param id
     * @return
     */
    @DeleteMapping(value = "/{bid}")
    public ResponseEntity<Response> delete(@PathVariable("bid") Long id, Model model) {
        System.out.println("进入删除图书的方法");
        try {
            bookService.removeBook(id);
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
        System.out.println("进入修改图书方法");
        Book book = bookService.getBookDetail(id);
        List<Category>  cateList= categoryDao.findAll();
        model.addAttribute("book", book);
        model.addAttribute("categoryList", cateList);
        return new ModelAndView("books/edit", "bookModel", model);
    }


}
