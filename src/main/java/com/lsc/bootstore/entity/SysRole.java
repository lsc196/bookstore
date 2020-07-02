package com.lsc.bootstore.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * 用户角色，即用户的权限
 */
@Entity
public class SysRole {

    @Id
    @GeneratedValue
    private Long id;

    //数据库角色必须以 ROLE_ 开头，如 ROLE_USER
    private String name;  //角色名称

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
