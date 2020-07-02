package com.lsc.bootstore.vo;

import lombok.Data;

@Data
public class SalasVo {
    private String  name;
    private long value;

    public SalasVo(){

    }

    public SalasVo(String name, long value) {
        this.name = name;
        this.value = value;

    }
}
