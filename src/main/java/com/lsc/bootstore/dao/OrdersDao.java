package com.lsc.bootstore.dao;

import com.lsc.bootstore.entity.Orders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

public interface OrdersDao extends JpaRepository<Orders,String> {
    Page<Orders> findAllByOrderByCreatedTimeDesc(Pageable pageable);
    Page<Orders> findAllByUserIdOrderByCreatedTimeDesc(Long userId, Pageable pageable);
    Page<Orders> findByOrderNoContains(String orderNo,Pageable pageable);
    Orders findByOrderNo(String orderNo);

    @Modifying
    @Transactional
    @Query("update Orders set status = '交易成功' where orderNo = :orderNo")
    int updateStatus(String orderNo);
}
