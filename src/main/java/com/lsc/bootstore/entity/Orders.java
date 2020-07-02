package com.lsc.bootstore.entity;

import com.lsc.bootstore.util.GeneratIDTool;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 在JPA规范中，一对多的双向关系由多端(Article)来维护。就是说多端(Article)为关系维护端，负责关系的增删改查。一端(Author)则为关系被维护端，不能维护关系。
 *
 * 一端(Author)使用@OneToMany注释的mappedBy="author"属性表明Author是关系被维护端。
 *
 * 多端(Article)使用@ManyToOne和@JoinColumn来注释属性 author,@ManyToOne表明Article是多端，@JoinColumn设置在article表中的关联字段(外键)。
 */
@Entity
public class Orders {
    @Id
    private String orderNo;
    private String status;
    private Long userId;
    private String userName;
    private String address;
    @Column(length = 11)
    private String phone;
    private Double totalSum;
    private Date createdTime;
    private Date updatedTime;
    //单向一对多
    @OneToMany(cascade= CascadeType.ALL,fetch= FetchType.LAZY)
    //级联保存、更新、删除、刷新;延迟加载。当删除用户，会级联删除该用户的所有文章
    @JoinColumn(name = "order_id")
    //通过在多的一方（OrderDetail）添加一个字段 order_no 做外键
    //referencedColumnName = "order_no" 对应 order 表的键，默认是主键，所以这个是多余的，可以不写
    private List<OrderDetail> orderDetailList;//文章列表


    public Orders(){

    }
    public Orders(String orderNo, String status, Long userId, String userName, String address, String phone, Double totalSum, Date createdTime, Date updatedTime) {
        this.orderNo = orderNo;
        this.status = status;
        this.userId = userId;
        this.userName = userName;
        this.address = address;
        this.phone = phone;
        this.totalSum = totalSum;
        this.createdTime = createdTime;
        this.updatedTime = updatedTime;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Double getTotalSum() {
        return totalSum;
    }

    public void setTotalSum(Double totalSum) {
        this.totalSum = totalSum;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    public List<OrderDetail> getOrderDetailList() {
        return orderDetailList;
    }

    public void setOrderDetailList(List<OrderDetail> orderDetailList) {
        this.orderDetailList = orderDetailList;
    }

    @Override
    public String toString() {
        return "Orders{" +
                "orderNo=" + orderNo +
                ", status='" + status + '\'' +
                ", userId=" + userId +
                ", userName='" + userName + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", totalSum=" + totalSum +
                ", createdTime=" + createdTime +
                ", updatedTime=" + updatedTime +
                ", orderDetailList=" + orderDetailList +
                '}';
    }
}
