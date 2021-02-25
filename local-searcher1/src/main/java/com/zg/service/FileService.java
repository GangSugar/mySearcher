package com.zg.service;

import com.zg.dao.DeleteDAO;
import com.zg.dao.QueryDAO;
import com.zg.dao.SaveDAO;
import com.zg.model.FileMeta;
import com.zg.util.ListUtil;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 保存文件的逻辑功能
 */
public class FileService {
    private SaveDAO saveDAO = new SaveDAO();
    private DeleteDAO deleteDAO = new DeleteDAO();

    private final QueryDAO queryDAO = new QueryDAO();
    /**
     * 给定文件，进行保存的
     * @param fileList
     */
    public void saveFileList(List<FileMeta> fileList){
        saveDAO.save(fileList);
    }

    /**
     * 给定文件进行删除操作
     * @param fileList
     */
    public void deleteFileList(List<FileMeta> fileList){
        List<Integer> idList = fileList.stream().map(FileMeta::getId).collect(Collectors.toList());
        deleteDAO.delete(idList);
    }

    /**
     *
     * //@param queryResultList 查询出来的文件
     * @param scanResultList 扫描出来的文件
     */
    public void service(String path, List<FileMeta> scanResultList) {
        List<FileMeta> queryResultList = queryDAO.queryByPath(path);

        // 1. queryResultList - scanResultList
        List<FileMeta> ds1 = ListUtil.differenceSet(queryResultList, scanResultList);
        deleteFileList(ds1);//删除

        // 2. scanResultList - queryResultList
        List<FileMeta> ds2 = ListUtil.differenceSet(scanResultList, queryResultList);
        saveFileList(ds2);//添加进去
    }


    public List<FileMeta> query(String keyword) {
        return queryDAO.query(keyword);
    }
}
