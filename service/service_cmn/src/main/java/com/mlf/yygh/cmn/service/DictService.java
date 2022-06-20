package com.mlf.yygh.cmn.service;

import com.mlf.yygh.model.cmn.Dict;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by Administrator on 2021/9/28.
 */
public interface DictService extends IService<Dict> {
    //根据数据id查询子数据列表
    List<Dict> findChildData(Long id);
    //导出数据字典接口  相当于下载
    void exportDictData(HttpServletResponse response);
    //导入数据字典   相当于上传功能
    void importDictData(MultipartFile file);

    //根据dictcode和value查询返回name值
    String getDictName(String dictCode, String value);

    //根据dictCode获取下级节点
    List<Dict> findByDictCode(String dictCode);
}
