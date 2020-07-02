package com.lsc.bootstore.controller;

import com.lsc.bootstore.dao.CartDao;
import com.lsc.bootstore.entity.Book;
import com.lsc.bootstore.entity.CartItem;
import com.lsc.bootstore.service.BookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

@Controller
public class BookController {
    @Autowired
    BookService bookService;
    @Autowired
    CartDao cartDao;

    @GetMapping("/books")
    public ModelAndView books( @RequestParam(value="pageIndex",required=false,defaultValue="0") int pageIndex,
                                  @RequestParam(value="pageSize",required=false,defaultValue="8") int pageSize,
                                  @RequestParam(value="name",required=false,defaultValue="") String name,
                               @RequestParam(value="sort",required=false,defaultValue="default") String sort,
                                  Model model){
        System.out.println("进入查找书本方法");
        Pageable pageable = null;
        if (sort.equals("default")){
            pageable = PageRequest.of(pageIndex, pageSize);
        }
        if (sort.equals("price")){
            pageable = PageRequest.of(pageIndex, pageSize,Sort.by("price").ascending());
        }
        if (sort.equals("salesVolume")){
            pageable = PageRequest.of(pageIndex, pageSize,Sort.by("salesVolume").descending());
        }
        Page<Book> page = bookService.listBookByNameLike(name,pageable);
        List<Book> bookList = page.getContent();
        model.addAttribute("page", page);
        model.addAttribute("bookList", bookList);
        model.addAttribute("bookNav",name);
        model.addAttribute("searchName",name);
        model.addAttribute("sort",sort);
        model.addAttribute("flag","seach");  //用于标识是分类查找还是通过书名查找
        return new ModelAndView("list","bookmodel",model);
    }

    @GetMapping("/bookCategory")
    public ModelAndView bookCategory( @RequestParam(value="pageIndex",required=false,defaultValue="0") int pageIndex,
                               @RequestParam(value="pageSize",required=false,defaultValue="8") int pageSize,
                               @RequestParam(value="category",required=false,defaultValue="") String category,
                               @RequestParam(value="sort",required=false,defaultValue="default") String sort,
                               Model model){
        Pageable pageable = null;
        if (sort.equals("default")){
            pageable = PageRequest.of(pageIndex, pageSize);
        }
        if (sort.equals("price")){
            pageable = PageRequest.of(pageIndex, pageSize,Sort.by("price").ascending());
        }
        if (sort.equals("salesVolume")){
            pageable = PageRequest.of(pageIndex, pageSize,Sort.by("salesVolume").descending());
        }
        Page<Book> page = bookService.listBookByCategory(category,pageable);
        List<Book> bookList=null;
        if (page != null){
            bookList = page.getContent();
        }

        model.addAttribute("page", page);
        model.addAttribute("bookList", bookList);
        model.addAttribute("searchName",null);
        model.addAttribute("bookNav",category);
        model.addAttribute("sort",sort);  //当前排序方式
        model.addAttribute("flag","category"); //用于标识是分类查找还是通过书名查找
        return new ModelAndView("list","bookmodel",model);
    }

    @GetMapping("/bookdesc")
    public ModelAndView bookdetails(@RequestParam(value="bid",required=false,defaultValue="") Long bid, Model model){
        System.out.println("进入查看书本详情方法BID="+bid);
        Book book = bookService.getBookDetail(bid);
        model.addAttribute("book", book);
        return new ModelAndView("bookdetail","bookmodel",model);
    }


}
