package com.zg.dao;

import com.zg.util.DBUtil;
import com.zg.util.LogUtil;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Scanner;

/**
 * 找打init.sql文件，并且读取内容，相当于建立数据库
 * 建表语句
 */
public class InitDAO {
    // 1.找到 init.sql 文件，并且读取其内容
    // 2.得到一组 SQL 语句 String[]
    // init.sql 是一组以 ';' 作为分割的多个 SQL 语句
    private String[] getSQLs(){
        try (InputStream is = InitDAO.class.getClassLoader().getResourceAsStream("init.sql")){
            StringBuilder sb = new StringBuilder();
            Scanner scan = new Scanner(is,"UTF-8");//将is作为输入流读进来，并且设置编码格式为UTF-8
            while (scan.hasNextLine()){//循环
                String line = scan.nextLine();//一行一行读取字符串
                sb.append(line);//拼接字符串
            }
            return sb.toString().split(";");//用；作为分割符，达到String数组
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    /**
     *创建数据库
     * 1.首先通过getSQL获取到sql语句
     */
    public void init() {
        try {
            Connection c = DBUtil.getConnection();

            String[] sqls = getSQLs();//1.得到sql语句
            for (String sql : sqls) {
                LogUtil.log("执行 SQL: " + sql);
                try (PreparedStatement s = c.prepareStatement(sql)) {
                    if (sql.toUpperCase().startsWith("SELECT ")) {
                        try (ResultSet resultSet = s.executeQuery()) {
                            ResultSetMetaData metaData = resultSet.getMetaData();
                            int columnCount = metaData.getColumnCount();//一共有多少列
                            int rowCount = 0;//行数
                            while (resultSet.next()) {//循环查询结果，针对每一行
                                StringBuilder message = new StringBuilder("|");
                                for (int i = 1; i <= columnCount; i++) {//针对每一列
                                    String value = resultSet.getString(i);
                                    message.append(value).append(" | ");
                                }
                                LogUtil.log(message.toString());
                                rowCount++;
                            }
                            LogUtil.log("一共查询出 %d 行", rowCount);
                        }
                    } else {
                        int i = s.executeUpdate();
                        LogUtil.log("受到影响的一共有 %d 行", i);

                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);//RuntimeException运行时异常(非受查(检查)异常)，这种异常不需要我们来出来
        }
    }

    public static void main(String[] args) {
        InitDAO initDAO = new InitDAO();
//        String[] s = initDAO.getSQLs();
//        System.out.println(s[1]);
//        System.out.println(s[0]);
//        System.out.println(Arrays.toString(s));
        initDAO.init();
    }
}
