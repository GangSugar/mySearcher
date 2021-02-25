package com.zg.service;

import com.zg.dao.InitDAO;
import com.zg.model.FileMeta;

import java.util.List;

public class DBService {
    /**
     * 1.可以查询
     * 主要作用，用户在在搜索框中输入待查询关键字的时候实现的逻辑功能
     */
    public List<FileMeta> queryByName(String name){
        return null;
    }

    private final InitDAO initDAO = new InitDAO();

    public void init() {
        initDAO.init();
    }
}
