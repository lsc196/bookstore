package com.lsc.bootstore.dao;


import com.lsc.bootstore.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface BookDao extends JpaRepository<Book,Long> {
    /**
     * 通过书名忽略大小写模糊查找
     * @param bname
     * @param pageable
     * @return
     */
    Page<Book> findByBnameIgnoreCaseContaining(String bname, Pageable pageable);
    Page<Book> findAllByCategoryId(int categoryId, Pageable pageable);

    Page<Book> findAllByOrderByPopularDesc(Pageable pageable);

    Page<Book> findAllByOrderBySalesVolumeDesc(Pageable pageable);

    @Modifying
    @Transactional
    @Query("update Book set salesVolume = salesVolume + 1 where bid = :bId")
    int addSalesVolume(Long bId);

}
