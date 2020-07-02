package com.lsc.bootstore.vo;

import lombok.Data;

@Data
public class ZheXianVo {
    private String day;
    private double value;

    public ZheXianVo(){

    }
    public ZheXianVo(String day, double value) {
        this.day = day;
        this.value = value;
    }
}
