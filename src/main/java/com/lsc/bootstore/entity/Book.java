package com.lsc.bootstore.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
@Data
public class Book implements Serializable {
    @Id
    @GeneratedValue
    private Long bid;//主键
    private String bname;//书名
    private String author;//作者
    private double price;//定价
    private String press;//出版社
    private String publishtime;//出版时间
    private String edition;//版次
    private String details; //详细
    private Integer categoryId;//所属分类ID
    private int salesVolume;  //销量
    private int popular; //热门等级
    private String image_w;//大图路径
    private String image_b;//小图路径

    public Book() {
    }

    public Book(String bname, String author, double price, String press, String publishtime, String edition, String details, Integer category_id, String image_w, String image_b) {
        this.bname = bname;
        this.author = author;
        this.price = price;
        this.press = press;
        this.publishtime = publishtime;
        this.edition = edition;
        this.details = details;
        this.categoryId = category_id;
        this.image_w = image_w;
        this.image_b = image_b;
        this.salesVolume=0;
    }

}
