package com.lsc.bootstore.service;

import com.lsc.bootstore.dao.OrderDetailDao;
import com.lsc.bootstore.vo.SalasVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class EchartService {
    @Autowired
    OrderDetailDao orderDetailDao;

    /**
     * 饼图，查询每个分类的销售数量
     * @return
     */
    public List<SalasVo> bingTu(String salaCatagory){
        if (salaCatagory.equals("all")){
            return orderDetailDao.findSalas();
        }
        List<SalasVo> salasVos= null;
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        if (salaCatagory.equals("oneMonth")){
            //过去一月
            c.add(Calendar.MONTH, -1);
        }else if (salaCatagory.equals("threeMonth")){
            c.add(Calendar.MONTH, -3);
        }
        else if (salaCatagory.equals("sixMonth")){
            c.add(Calendar.MONTH, -6);
        }
        else if (salaCatagory.equals("oneYear")){
            c.add(Calendar.YEAR, -1);
        }
        Date m = c.getTime();
        salasVos = orderDetailDao.findSalasOneMonth(m);
        return salasVos;
    }

    public List<Object[]> findDaySalas(){
        return orderDetailDao.findDaySalas();
    }

}
