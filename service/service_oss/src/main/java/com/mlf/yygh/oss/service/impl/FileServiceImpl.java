package com.mlf.yygh.oss.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.mlf.yygh.oss.service.FileService;
import com.mlf.yygh.oss.utils.ConstantOssPropertiesUtils;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * Created by Administrator on 2021/12/3.
 * 上传文件到阿里云OSS里接口的开发  另作两个完善，第一个加上uuid值，第二个完善按照文件夹进行分类
 */
@Service
public class FileServiceImpl implements FileService {

    /** 上传文件到阿里云 **/ //返回上传文件路径
    @Override
    public String upLoad(MultipartFile file) {
        String endpoint = ConstantOssPropertiesUtils.ENDPOINT;
        String accessKeyId = ConstantOssPropertiesUtils.ACCESS_KEY_ID;
        String accessKeySecret = ConstantOssPropertiesUtils.SECRECT;
        String bucketName = ConstantOssPropertiesUtils.BUCKET;
        try {
            String fileName = file.getOriginalFilename();// 填写文件名。文件名包含路径
            String uuid = UUID.randomUUID().toString().replaceAll("-","");  //生成随机唯一值
            fileName = uuid + fileName; //注意：uuid放在前面
            String timeUrl = new DateTime().toString("yyyy/MM/dd");//按照当前日期，创建文件夹，把文件上传到创建的文件夹里面
            fileName =timeUrl+"/"+fileName;                             //  2021/2/2/01.jpg
            OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);// 创建OSSClient实例。
            InputStream inputStream = file.getInputStream();  //上传文件流
            ossClient.putObject(bucketName, fileName, inputStream); //调用方法实现上传第一个值就是bucket名称，第二个值指文件名称加路径
            ossClient.shutdown(); // 关闭OSSClient。
            String url = "https://" + bucketName + "." + endpoint +"/"+ fileName;   //返回上传文件路径
            return url;
        } catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }
}
