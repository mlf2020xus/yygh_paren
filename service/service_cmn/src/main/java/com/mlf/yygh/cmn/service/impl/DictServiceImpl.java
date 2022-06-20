package com.mlf.yygh.cmn.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.mlf.yygh.model.cmn.Dict;
import com.mlf.yygh.vo.cmn.DictEeVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mlf.yygh.cmn.listener.DictListener;
import com.mlf.yygh.cmn.mapper.DictMapper;
import com.mlf.yygh.cmn.service.DictService;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2021/9/28.
 */
@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {

    //将返回的数据放入到缓存中  //根据数据id查询子数据列表
    @Cacheable(value = "dict", keyGenerator = "keyGenerator")
    @Override
    public List<Dict> findChildData(Long id) {
        QueryWrapper<Dict> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id",id);
        List<Dict> dictList = baseMapper.selectList(wrapper);
        //向list集合每个dict对象中设置hasChildren
        for(Dict dict:dictList){
            Long dictId = dict.getId();
            boolean isChild = this.isChilden(dictId);
            dict.setHasChildren(isChild);
        }
        return dictList;
    }

    //导出数据字典接口  相当于下载功能  写出
    @Override
    public void exportDictData(HttpServletResponse response) {
        //1、设置下载的相关信息
        //设置文档类型
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String fileName = "dict";
        response.setHeader("Content-disposition", "attachment;filename="+ fileName + ".xlsx");
        //2、查询数据库
        List<Dict> dictList = baseMapper.selectList(null);
        //Dict -- DictEeVo
        List<DictEeVo>  dictVoList = new ArrayList<>();
        for(Dict dict:dictList){
            DictEeVo dictEeVo = new DictEeVo();
            BeanUtils.copyProperties(dict,dictEeVo);
            dictVoList.add(dictEeVo);
        }
        //调用方法进行写操作
        try {
            EasyExcel.write(response.getOutputStream(),DictEeVo.class).sheet("dict")
                        .doWrite(dictVoList);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    //导入数据字典接口  相当于上传功能  读入
    @CacheEvict(value = "dict", allEntries = true)
    @Override
    public void importDictData(MultipartFile file) {
        //上传文件需要一个监听器  DictListener表示监听器
        try {
            EasyExcel.read(file.getInputStream(),DictEeVo.class,new DictListener(baseMapper)).sheet().doRead();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //根据dictcode和value查询返回name值(指医院等级名称)
    @Override
    public String getDictName(String dictCode, String value) {
        //dictCode空值判断, 若为空，直接根据value查询
        if(StringUtils.isEmpty(dictCode)){//dictCode若为空,直接根据value查询
            //QueryWrapper是mp的条件封装对象
            QueryWrapper<Dict> wrapper = new QueryWrapper<>();
            wrapper.eq("value",value);
            Dict dict = baseMapper.selectOne(wrapper);
            return dict.getName();
        }else {//dictCode不为空,根据dictCode和value查询
            //根据dictCode查询Dict对象，得到dict的id
            QueryWrapper<Dict> wrapper = new QueryWrapper<>();
            wrapper.eq("dict_code",dictCode);
            Long parent_id = baseMapper.selectOne(wrapper).getId();
            //根据parent_id和value进行查询
            wrapper.eq("parent_id",parent_id);
            wrapper.eq("value",value);
            Dict dict = baseMapper.selectOne(wrapper);
            return dict.getName();
        }
    }
    //根据dictCode获取下级节点
    @Override
    public List<Dict> findByDictCode(String dictCode) {
        //根据dictCode获取相应id
        Dict dict = this.getDictByDictCode(dictCode);
        //根据id来获取子节点
        List<Dict> childData = this.findChildData(dict.getId());
        return childData;
    }
    private Dict getDictByDictCode(String dictCode){
        QueryWrapper<Dict> wrapper = new QueryWrapper<>();
        wrapper.eq("dict_code",dictCode);
        Dict dict = baseMapper.selectOne(wrapper);
        return dict;
    }
    //判断id下面是否有子节点
    private boolean isChilden(Long id){
        QueryWrapper<Dict> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id",id);
        Integer count = baseMapper.selectCount(wrapper);
        //count>0   0>0  1>0   直接返回值就是true 或者 false
        return count>0;
    }
}
