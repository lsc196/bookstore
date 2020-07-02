package com.lsc.bootstore.dao;

import com.lsc.bootstore.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryDao extends JpaRepository<Category,Integer> {
    Page<Category> findByCnameLike(String cname, Pageable pageable);

    Category findByCname(String cname);
}
