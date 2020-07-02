package com.lsc.bootstore.alipay;

import com.alipay.api.AlipayApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

@Controller
@RequestMapping("/payment")
public class AlipayController {

    @Autowired
    AlipayService alipayService;

    @RequestMapping("/pay")
    @ResponseBody
    public void payMent(HttpServletResponse response, HttpServletRequest request) {
        try {
            alipayService.aliPay(response,request);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("/payByOrderNo")
    @ResponseBody
    public void payMentByOrderNo(HttpServletResponse response, HttpServletRequest request,String orderNo) {
        try {
            alipayService.aliPayByOrderNo(response,request,orderNo);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("/notify_url")
    @ResponseBody
    public String notify(HttpServletRequest request) throws AlipayApiException, UnsupportedEncodingException {
       return alipayService.notify(request);
    }

    @RequestMapping("/return_url")
    public String Return_url() throws InterruptedException {
        System.out.println("支付返回首页");
        return "redirect:/index";
    }


}
