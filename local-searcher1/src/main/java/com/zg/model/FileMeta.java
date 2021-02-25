package com.zg.model;

import com.zg.util.OutputUtil;
import com.zg.util.PinYinUtil;

import java.io.File;
import java.util.Objects;

/**
 * 文件模型类
 */
public class FileMeta {
    //加finall代表属性只能初始化一次
    private final Integer id;//文件id
    private final String name;//文件名称
    private final String pinyin;
    private final String pinyinFirst;
    private final String path;//文件路径
    private final boolean directory;//判断是否是文件夹
    private final Long length;//文件长度
    private final long lastModifiedTimestamp;//文件最后修改时间

    /**
     * 下面提供俩个构造方法，一个给文件用，一个给数据库中用
     */

    //给扫描文件提供的构造方法
    public FileMeta(File file){
        this.id = null;
        this.name = file.getName();//获取文件的名称
        this.pinyin = PinYinUtil.getPinYin(name);
        this.pinyinFirst = PinYinUtil.getPinYinFirst(name);
        //this.path = file.getAbsolutePath();//获取的是绝对路径(这个是单线程下需要获取到存入文件的绝对路径)
        this.path = file.getParent();//这个是为了配合多线程，只获取到父文件的绝对路径即可
        this.directory = file.isDirectory();//判断是否是文件夹
        this.length = file.length();//文件大小
        this.lastModifiedTimestamp = file.lastModified();//文件最后修改时间
    }

    //给数据库提供的构造方法
    public FileMeta(Integer id, String name,String pinyin, String pinyinFirst, String path, boolean directory, Long length, Long lastModifiedTimestamp){
        this.id = id;
        this.name = name;
        this.pinyin = pinyin;
        this.pinyinFirst = pinyinFirst;
//        this.pinyin = PinYinUtil.getPinYin(name);
//        this.pinyinFirst = PinYinUtil.getPinYinFirst(name);
        this.path = path;
        this.directory = directory;
        this.length = length;
        this.lastModifiedTimestamp = lastModifiedTimestamp;
    }

    //私有的获取数据方法

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    public String getPinyin() {
        return pinyin;
    }

    public String getPinyinFirst() {
        return pinyinFirst;
    }
    public String getPath() {
        return path;
    }

    public boolean isDirectory() {
        return directory;
    }

    public Long getLength() {
        return length;
    }

    public String getLengthUI() {
        return OutputUtil.formatLength(length);
    }

    public Long getLastModifiedTimestamp() {
        return lastModifiedTimestamp;
    }

    public String getLastModifiedUI() {
        //return String.valueOf(lastModifiedTimestamp);
        return OutputUtil.formatTimestamp(lastModifiedTimestamp);
    }

    @Override
    public String toString() {
        return "FileMeta{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", pinyin='" + pinyin + '\'' +
                ", pinyinFirst='" + pinyinFirst + '\'' +
                ", path='" + path + '\'' +
                ", directory=" + directory +
                ", length=" + length +
                ", lastModifiedTimestamp=" + lastModifiedTimestamp +
                '}';
    }

    // 只要两个 FileMeta 对象的 path 一样，就是代表同一个文件
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileMeta fileMeta = (FileMeta) o;
        return path.equals(fileMeta.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path);
    }
}
