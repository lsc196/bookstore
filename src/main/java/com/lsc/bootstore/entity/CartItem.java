package com.lsc.bootstore.entity;

import lombok.Data;

@Data
public class CartItem {
    private int cart_id;
    private Long user_id;
    private Long b_id;
    private String img_url;
    private String bname;
    private String author;
    private String press;
    private double price;
    private Integer quantity;
    private double sum;

    public CartItem(){

    }

    public CartItem(int cart_id, Long user_id, Long b_id, String img_url, String bname, String author, String press, double price, Integer quantity) {
        this.cart_id = cart_id;
        this.user_id = user_id;
        this.b_id = b_id;
        this.img_url = img_url;
        this.bname = bname;
        this.author = author;
        this.press = press;
        this.price = price;
        this.quantity = quantity;
        sum = (Math.round((price*100)*quantity))/100.0;
    }
}
