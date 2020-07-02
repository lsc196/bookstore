package com.lsc.bootstore.service;

import com.lsc.bootstore.dao.CartDao;
import com.lsc.bootstore.entity.Cart;
import com.lsc.bootstore.entity.CartItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CartService {
    @Autowired
    CartDao cartDao;

    public int cartAdd(Long userId,Long bid,int quantity){
        //首先判断是否存在相同产品在购物车
        Cart cart = cartDao.findByUserIdAndProductId(userId,bid);
        //没有就添加一个产品进购物车
        if(cart == null){
            Cart cart1 = new Cart(userId,bid,quantity,1,new Date(),new Date());
            cartDao.save(cart1);
        }else {
            int t = cart.getQuantity();
            cart.setQuantity(t+quantity);
            cartDao.save(cart);
        }
        return 1;
    }

    public int QuantityAdd(int cartId){
        int result = cartDao.addQuantityByCartId(cartId);
        return result;
    }

    public int QuantityDec(int cartId){
        int result = cartDao.decQuantityByCartId(cartId);
        return result;
    }

    public List<CartItem> getCartListByUserId(Long userId){
        List<CartItem> carts = cartDao.findCartItemByUserid(userId);
        return carts;
    }

    public void deleteCart(int cartId){
        cartDao.deleteById(cartId);
    }

}
