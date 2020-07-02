package com.lsc.bootstore.controller.backend;

import com.lsc.bootstore.service.EchartService;
import com.lsc.bootstore.vo.Response;
import com.lsc.bootstore.vo.SalasVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RequestMapping("/manage/statistics")
@Controller
public class StatisticsController {

    @Autowired
    EchartService echartService;

    @GetMapping
    public ModelAndView  view(){
        System.out.println("统计");
        return new ModelAndView("statistics/echarts");
    }

    @GetMapping("/bingtu")
    @ResponseBody
    public Response bingTu(String salaCatagory){
        List<SalasVo> salasVos = echartService.bingTu(salaCatagory);
        //总销售量
        long sum = 0;
        for (SalasVo s:salasVos) {
            sum += s.getValue();
        }
        HashMap<String,Object> map = new HashMap<>();
        map.put("salasVos",salasVos);
        map.put("sum",sum);
        return new Response(true,"",map);
    }

    @GetMapping("/zhexiantu")
    @ResponseBody
    public Response zhexianTu(){
        List<Object[]> list = echartService.findDaySalas();
        // X 轴名称
        List<String> xAxis = new ArrayList<>();
        // 数据
        List<Double> datas = new ArrayList<>();
        for (int i =list.size()-1;i>=0;i--) {
            Object[] objs = list.get(i);
            String str =(String)objs[0];
            //str为202003.18，取后5位，即03.18
            xAxis.add(str.substring(str.length()-5));
            datas.add((Double) objs[1]);
        }
        HashMap<String,List> map = new HashMap<>();
        map.put("xAxis",xAxis);
        map.put("datas",datas);
        return new Response(true,"",map);
    }

}
