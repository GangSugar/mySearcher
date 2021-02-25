package com.zg.dao;

import com.zg.util.DBUtil;
import com.zg.util.LogUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DeleteDAO {
    /**
     * 数据库中的删除操作
     * 分析，因为id是唯一的，所以根据传入需要删除文件的id
     */
    public void delete(List<Integer> idList){
        Connection c = DBUtil.getConnection();// 由于实现成，一个线程只有一个 Connection 对象了，所以不需要关闭连接
            /**
             * stream是jdk1.8中特性
             * List<String> list = new ArrayList<>();
             * for(Integer id : idList){
             *      list.add("?");
             * }
             * 1.首先拿到Stream对象，map是针对idList的每一项，id映射成“？”
             */
            List<String> placeholdList = idList.stream()
                    .map(id -> "?")
                    .collect(Collectors.toList());
            //String.join()字符串拼接,将传进来的数组或者链表表中的值，按照顺序和给定符号连接起来，比如下面的逗号1，2，3
            String placeHolder = String.join(",",placeholdList);
            //2.写sql语句，根据id进行删除操作
        //String.format将，后面的值，替代前面的变量，就像printf替代一样，%d是整数，%s是字符串
            String sql = String.format("delete from file_meta where id in (%s)",placeHolder);
            //System.out.println(sql);
        try(PreparedStatement s = c.prepareStatement(sql)){
            for (int i = 0; i < idList.size(); i++) {
                int id = idList.get(i);
                s.setInt(i + 1, id);
            }
            LogUtil.log("执行 SQL: %s, %s", sql, idList);
            s.executeUpdate();
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public static void main(String[] args) throws SQLException{
        List<Integer> idList = Arrays.asList(1,2,3,4,5);

        DeleteDAO dao = new DeleteDAO();

        dao.delete(idList);
    }
}
