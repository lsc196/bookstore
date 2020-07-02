package com.lsc.bootstore.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
@Component
public class GeneratIDTool {
//    private static int sequence = 0;
    private static int length = 5;
    @Autowired
    RedisUtil redisUtil;

    /**
     * 根据日期跟流水号生成订单号
     * @return
     */
    public  synchronized String getLocalTrmSeqNum() {
        long sequence = redisUtil.incr("order_sequence",1);
        sequence = sequence >= 99999 ? 1 : sequence;
        String datetime = new SimpleDateFormat("yyyyMMdd")
                .format(new Date());
        String s = Long.toString(sequence);
        String result =datetime +addLeftZero(s, length);
        return result;
    }

    /**
     * 左填0
     * @param s
     * @param length 长度
     * @return
     */
    public static String addLeftZero(String s, int length) {
        // StringBuilder sb=new StringBuilder();
        int old = s.length();
        if (length > old) {
            char[] c = new char[length];
            char[] x = s.toCharArray();
            if (x.length > length) {
                throw new IllegalArgumentException(
                        "Numeric value is larger than intended length: " + s
                                + " LEN " + length);
            }
            int lim = c.length - x.length;
            for (int i = 0; i < lim; i++) {
                c[i] = '0';
            }
            System.arraycopy(x, 0, c, lim, x.length);
            return new String(c);
        }
        return s.substring(0, length);

    }

}
