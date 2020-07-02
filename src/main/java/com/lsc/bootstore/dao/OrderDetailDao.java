package com.lsc.bootstore.dao;

import com.lsc.bootstore.entity.OrderDetail;
import com.lsc.bootstore.entity.Orders;
import com.lsc.bootstore.vo.SalasVo;
import com.lsc.bootstore.vo.ZheXianVo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public interface OrderDetailDao extends JpaRepository<OrderDetail,Long> {

    /**
     * 查找各类全部图书销售数量
     * @return
     */
    @Query("select new com.lsc.bootstore.vo.SalasVo(c.cname,count(1)) from OrderDetail o,Category c where o.productCategory = c.id group by o.productCategory")
    List<SalasVo> findSalas();

    /**
     * 按时间段查找各类图书销售量
     * @param date
     * @return
     */
    @Query("select new com.lsc.bootstore.vo.SalasVo(c.cname,count(1)) from OrderDetail o,Category c where o.productCategory = c.id and o.createdTime >= :date group by o.productCategory")
    List<SalasVo> findSalasOneMonth(Date date);

    /**
     * 查询每天的销售额，按天分组查询
     * 使用原生sql语句查询，,nativeQuery = true，默认是false
     * @return
     */
    @Query(value = "select DATE_FORMAT(created_time,'%y%y%m.%d') days,sum(total_price) sum from order_detail group by days DESC  limit 15",nativeQuery = true)
    List<Object[]> findDaySalas();

    /**
     * 方法二，自定义映射
     * @return
     */
//    @Query(value = "select new com.lsc.bootstore.vo.ZheXianVo(DATE_FORMAT(createdTime,'%m.%d'),sum(totalPrice))from OrderDetail group by DATE_FORMAT(createdTime,'%m.%d')")
//    List<ZheXianVo> findDaySalas();
}
