package com.zg.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日志工具类
 */
public class LogUtil {
    // 类型... 可变参数列表
    // Object... 代表，任意类型，任意长度都可以传入
    public static void log(String format, Object... args){
        String message = String.format(format,args);//错误消息,String.format(format,args);args替代的是format中%d那些东西，类似于printf
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//时间戳
        Date date = new Date();
        String now = dateFormat.format(date);//now当前时间
        System.out.printf("%s: %s\n", now, message);//打印时间和错误消息
        //System.out.println(Arrays.toString(args));
    }

    public static void main(String[] args) {
//        log(" ",123);
//        log(" ");
//        log("%s,%d",12,1123);//这样写类似于printf
        log("你好");
        log("今天有%d个同学来听课",18);
    }
}
