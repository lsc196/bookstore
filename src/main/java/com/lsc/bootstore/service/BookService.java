package com.lsc.bootstore.service;

import com.lsc.bootstore.dao.BookDao;
import com.lsc.bootstore.dao.CategoryDao;
import com.lsc.bootstore.entity.Book;
import com.lsc.bootstore.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    @Autowired
    BookDao bookDao;
    @Autowired
    CategoryDao categoryDao;

    public Page<Book> listBookByNameLike(String name, Pageable pageable){
        Page<Book> booklist = bookDao.findByBnameIgnoreCaseContaining(name,pageable);
        return booklist;
    }

    public Page<Book> listBookByCategory(String category, Pageable pageable){
        Category c = categoryDao.findByCname(category);
        if (c == null){
            return null;
        }
        int categoryId =c.getId();
        Page<Book> booklist = bookDao.findAllByCategoryId(categoryId,pageable);
        return booklist;
    }

    public Book getBookDetail(Long id){
        Optional<Book> list = bookDao.findById(id);
        return list.get();
    }

    /**
     * 使用Redis缓存
     * @param book
     */
    public void saveBook(Book book){
        bookDao.save(book);
    }

    public void removeBook(Long bid){
        bookDao.deleteById(bid);
    }

    /**
     * 热门书籍，通过销量对比，销量越高排越前
     * @Cacheable 使用Redis缓存结果
     * @param pageable
     * @return
     */
    @Cacheable(value = "hotBooks")
    public Page<Book> hotBooks(Pageable pageable){
        return bookDao.findAllByOrderBySalesVolumeDesc(pageable);
    }

    /**
     * 推荐书籍，通过热门指数来排
     * @param pageable
     * @return
     */
    @Cacheable(value = "recommendBooks")
    public Page<Book> recommendBooks(Pageable pageable){
        return bookDao.findAllByOrderByPopularDesc(pageable);
    }

     public int addSalesVolume(Long bId){
        return bookDao.addSalesVolume(bId);
     }
}
