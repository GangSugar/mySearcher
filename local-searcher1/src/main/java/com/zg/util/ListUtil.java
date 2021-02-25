package com.zg.util;

import com.zg.model.FileMeta;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListUtil {
    // 写一个泛型方法
    // list1 - list2

    /**
     * 计算出来俩个的差集
     * @param list1
     * @param list2
     * @param <E>
     * @return
     */
    public static <E> List<E> differenceSet(List<E> list1, List<E> list2) {
        List<E> resultList = new ArrayList<>();
//遍历第一个链表，如果第二个链表中不存在，那么就将这个文件添加到结果集中
        for (E item : list1) {
            if (!list2.contains(item)) {    // E 类型必须正确地支持 equals 方法
                resultList.add(item);
            }
        }

        return resultList;
    }

    public static void main(String[] args) {
        List<FileMeta> list1 = Arrays.asList(
                new FileMeta(new File("D:\\课程\\2021-1-17-春招冲刺班-项目1\\板书1.png")),
                new FileMeta(new File("D:\\课程\\2021-1-17-春招冲刺班-项目1\\板书2.png")),
                new FileMeta(new File("D:\\课程\\2021-1-17-春招冲刺班-项目1\\板书3.png"))
        );


        List<FileMeta> list2 = Arrays.asList(
                new FileMeta(new File("D:\\课程\\2021-1-17-春招冲刺班-项目1\\板书2.png")),
                new FileMeta(new File("D:\\课程\\2021-1-17-春招冲刺班-项目1\\板书3.png")),
                new FileMeta(new File("D:\\课程\\2021-1-17-春招冲刺班-项目1\\板书4.png"))
        );

        for (FileMeta fm : differenceSet(list1, list2)) {    // 1
            System.out.println(fm);
        }
        System.out.println("=============================");
        for (FileMeta fm : differenceSet(list2, list1)) {    // 4
            System.out.println(fm);
        }
    }
}

