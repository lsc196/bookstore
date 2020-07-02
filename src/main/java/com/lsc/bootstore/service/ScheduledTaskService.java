package com.lsc.bootstore.service;

import com.lsc.bootstore.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class ScheduledTaskService {

    @Autowired
    RedisUtil redisUtil;


    /**
     * 每天0点更新
     */
    @Scheduled(cron = "0 0 00 * * ?")
    public void resetOrderNo(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
        System.out.println("更新订单序列号，现在的时间是;"+dateFormat.format(new Date()));
        redisUtil.set("order_sequence",0);
    }
}
