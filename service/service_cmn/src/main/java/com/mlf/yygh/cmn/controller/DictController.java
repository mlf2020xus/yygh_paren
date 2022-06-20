package com.mlf.yygh.cmn.controller;

import com.mlf.yygh.model.cmn.Dict;
import com.mlf.yygh.cmn.service.DictService;
import com.mlf.yygh.common.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by Administrator on 2021/10/3.
 */
@Api(value = "数据字典接口")
@RestController
@RequestMapping("/admin/cmn/dict")
//@CrossOrigin//在控制层加这个注解，解决跨域问题    配置了网关的配置类，不需要这个注解了，以免出错
public class DictController {

    @Autowired
    private DictService dictService;

    //导入数据字典   MultipartFile是来获取上传的文件对象  读入
    @PostMapping("importData")
    public void importData(MultipartFile file) {
        dictService.importDictData(file);
    }
    //导出数据字典接口   相当于下载操作  写出
    @GetMapping("exportData")
    public Result exportData(HttpServletResponse response) {
        dictService.exportDictData(response);
        return Result.ok();
    }
    //根据dictCode获取下级节点
    @ApiOperation("根据dictCode获取下级节点")
    @GetMapping("findByDictCode/{dictCode}")
    public Result findByDictCode(@PathVariable("dictCode") String dictCode){
       List<Dict> list = dictService.findByDictCode(dictCode);
       return Result.ok(list);
    }
    //根据数据id查询子数据列表
    @ApiOperation(value = "根据数据id查询子数据列表")
    @GetMapping("findChildData/{id}")
    public Result findChildData(@PathVariable("id") Long id){
      List<Dict> list = dictService.findChildData(id);
        return Result.ok(list);
    }

    //根据dictcode和value查询返回name值
    @GetMapping("getName/{dictCode}/value")
    public String getName(@PathVariable String dictCode,
                          @PathVariable String value){
      String dictName =  dictService.getDictName(dictCode,value);
        return dictName;
    }
    //根据value查询，直接查询name值，
    @GetMapping("getName/value")
    public String getName(@PathVariable String value){
        String dictName =  dictService.getDictName("",value);
        return dictName;
    }
}
