package com.zg.dao;

import com.zg.model.FileMeta;
import com.zg.util.DBUtil;
import com.zg.util.LogUtil;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

/*
SaveDAO：
最终用户/场景：保存扫描出的文件列表
插入数据
 */
public class SaveDAO {
    /**
     * 插入文件
     * @param fileList
     */
    public void save(List<FileMeta> fileList) {
        try {
            String sql = "INSERT INTO file_meta " +
                    "(name, path, is_directory, pinyin, pinyin_first, size, last_modified) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";
            Connection c = DBUtil.getConnection();

            try (PreparedStatement s = c.prepareStatement(sql)) {
                // 一个一个文件插入 OR 批量插入
                for (FileMeta file : fileList) {//用for循环一个一个导入
                    s.setString(1, file.getName());//设置7个占位符的值
                    s.setString(2, file.getPath());
                    s.setBoolean(3, file.isDirectory());
                    s.setString(4, file.getPinyin());
                    s.setString(5, file.getPinyinFirst());
                    s.setLong(6, file.getLength());
                    s.setLong(7, file.getLastModifiedTimestamp());

                    int i = s.executeUpdate();//插入之后返回的结果
                    LogUtil.log("执行 SQL: %s, %s, 收到影响的行数是: %d", sql, file, i);//打印日志
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws IOException {
//        File file = new File("D:\\课程\\2021-1-17-春招冲刺班-项目1\\板书1.png");
//        Path path = FileSystems.getDefault().getPath(file.getAbsolutePath());
//        System.out.println(path);
//        BasicFileAttributes basicFileAttributes = Files.readAttributes(path, BasicFileAttributes.class);
//        System.out.println(basicFileAttributes.size());
//        System.out.println(file.length());
//        System.out.println(file.lastModified());


        List<FileMeta> fileList = Arrays.asList(
                new FileMeta(new File("D:\\课程\\2021-1-17-春招冲刺班-项目1\\板书1.png")),
                new FileMeta(new File("D:\\课程\\2021-1-17-春招冲刺班-项目1\\板书2.png")),
                new FileMeta(new File("D:\\课程\\2021-1-17-春招冲刺班-项目1\\板书3.png")),
                new FileMeta(new File("D:\\课程\\2021-1-17-春招冲刺班-项目1\\板书4.png"))
        );

        SaveDAO saveDAO = new SaveDAO();
        saveDAO.save(fileList);//测试
    }
}
