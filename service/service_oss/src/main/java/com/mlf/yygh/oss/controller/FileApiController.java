package com.mlf.yygh.oss.controller;

import com.mlf.yygh.oss.service.FileService;
import com.mlf.yygh.common.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by Administrator on 2021/12/3.
 *
 * 文件上传到OSS
 */
@RestController
@RequestMapping("/api/oss/file")
public class FileApiController {

    @Autowired
    FileService fileService;


    /** 上传文件到阿里云OSS**/
    @PostMapping("fileUpload")
    public Result fileUpload(MultipartFile file){
        //MultipartFile该对象是用作上传文件的对象
       String url = fileService.upLoad(file);
        return Result.ok(url);
    }
}
