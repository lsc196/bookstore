package com.lsc.bootstore.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class Category implements Serializable {
    @Id
    @GeneratedValue
    private Integer id; //主键
    private String cname; //分类名称
    private String descrise;  //分类描述

    public Category(){

    }
    public Category(String cname, String desc) {
        this.cname = cname;
        this.descrise = desc;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getDescrise() {
        return descrise;
    }

    public void setDescrise(String descrise) {
        this.descrise = descrise;
    }
}
