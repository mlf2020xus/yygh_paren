package com.mlf.yygh.oss.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * Created by Administrator on 2021/12/3.
 */
public interface FileService {

    /** 上传文件到阿里云 **/
    String upLoad(MultipartFile file);
}
