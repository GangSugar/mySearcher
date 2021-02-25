package com.zg.util;

import org.sqlite.SQLiteDataSource;

import javax.sql.DataSource;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * 数据库工具类
 * 就是JDBC
 */
public class DBUtil {
    private static volatile DataSource dataSource = null;//volatile关键字用来保证有序性

    /**
     * 下面这个
     * @returngetDatabasePath()方法
     * 是用来获取路径的，获取的是resource下面的那个init.sql里面的初始化数据库的代码
     */
    private static String getDatabasePath() {
        try {
            String classesPath = DBUtil.class.getProtectionDomain()
                    .getCodeSource().getLocation().getFile();
            File classesDir = new File(URLDecoder.decode(classesPath, "UTF-8"));
            String path = classesDir.getParent() + File.separator + "searcher.db";
            LogUtil.log("数据库文件路径为: %s", path);
            return path;
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }


    public static void initDataSource(){
        SQLiteDataSource sqLiteDataSource = new SQLiteDataSource();
        String url = "jdbc:sqlite://"+getDatabasePath();
        sqLiteDataSource.setUrl(url);//设置数据库池的url
        dataSource = sqLiteDataSource;
    }

    /**
     * 这里使用单例模式——懒汉模式(碰到他的时候才进行初始化)
     * @return
     */

    public static Connection initConnection () {
        if (dataSource == null) {
            synchronized (DBUtil.class) {
                if (dataSource == null) {
                    initDataSource();
                }
            }
        }
        //Connection 对象是线程不安全 —— 每个线程都必须有自己的 Connection 对象
        // 一个线程只有一个 Connection 对象没有问题
        // 使用 ThreadLocal 实现，每个线程有自己的 Connection 对象
        try {
            return dataSource.getConnection();

        } catch (SQLException throwables) {
            throw new RuntimeException(throwables);
        }
    }

    /**
     * ThreadLocal这个类实现每一个线程都有自己的Connection
     */
    private static final ThreadLocal<Connection> connectionThreadLocal = ThreadLocal.withInitial(DBUtil::initConnection);

    public static Connection getConnection() {
        return connectionThreadLocal.get();//给每一个线程都有自己的CAonnection对象

    }
}
