package com.lsc.bootstore.alipay;

import com.alipay.api.AlipayApiException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public interface AlipayService {

    /**
     * 支付宝支付调用接口
     * @param response
     * @param request
     * @throws IOException
     */
    void  aliPay(HttpServletResponse response, HttpServletRequest request) throws IOException;

    /**
     * 支付宝支付成功异步通知接口
     * @param request
     * @return
     * @throws AlipayApiException
     * @throws UnsupportedEncodingException
     */
     String notify(HttpServletRequest request) throws AlipayApiException, UnsupportedEncodingException;

     void aliPayByOrderNo(HttpServletResponse response, HttpServletRequest request,String orderNo) throws IOException;
}
