package com.zg.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 输出工具类
 */
public class OutputUtil {
    public static String formatLength(Long length) {
        if (length < 1024){
            return length+"Byte";
        }
        if (length < 1024*1024){
            return (length/1024)+"KB";
        }
        if (length < 1024*1024*1024){
            return (length/1024/1024)+"MB";
        }
        return (length/1024/1024/1024)+"GB";
    }

    public static String formatTimestamp(long lastModifiedTimestamp) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date = new Date(lastModifiedTimestamp);
        return format.format(date);
    }
}
