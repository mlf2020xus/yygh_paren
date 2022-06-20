package com.mlf.yygh.cmn.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.mlf.yygh.model.cmn.Dict;
import com.mlf.yygh.vo.cmn.DictEeVo;
import com.mlf.yygh.cmn.mapper.DictMapper;
import org.springframework.beans.BeanUtils;

/**
 * Created by Administrator on 2021/10/3.
 * 这个类时为上传文件时做监听   监听器类
 */
public class DictListener extends AnalysisEventListener<DictEeVo> {

    private DictMapper dictMapper;

    public DictListener(DictMapper dictMapper) {
        this.dictMapper = dictMapper;
    }

    //从第二行一行一行的读取
    /** dictEeVo每次读取到的内容，把它加载到数据库就可以了  **/
    @Override
    public void invoke(DictEeVo dictEeVo, AnalysisContext analysisContext) {
        //调用方法添加数据库
        Dict dict = new Dict();
        BeanUtils.copyProperties(dictEeVo,dict);
        dictMapper.insert(dict);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
