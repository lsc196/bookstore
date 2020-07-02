package com.lsc.bootstore.dao;

import com.lsc.bootstore.entity.Cart;
import com.lsc.bootstore.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CartDao  extends JpaRepository<Cart,Integer> {
    /**
     * 通过用户id查找购物车列表
     * @param userid
     * @return
     */
    @Query(value = "select new com.lsc.bootstore.entity.CartItem(c.id,c.userId,c.productId,b.image_w,b.bname,b.author,b.press,b.price,c.quantity) "
            + "from Cart c, Book b where c.productId = b.bid and"
            + " c.userId = :userid")
    List<CartItem> findCartItemByUserid(@Param("userid") Long userid);

    Cart findByUserIdAndProductId(Long userId,Long productId);

    @Modifying
    @Transactional
    @Query("update Cart set quantity = quantity + 1 where id = :cartId")
    int addQuantityByCartId(Integer cartId);

    @Modifying
    @Transactional
    @Query("update Cart set quantity = quantity - 1 where id = :cartId and quantity >1")
    int decQuantityByCartId(Integer cartId);

}
