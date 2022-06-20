package com.mlf.easyexcel;

import com.alibaba.excel.EasyExcel;

/**
 * Created by Administrator on 2021/10/3.
 */
public class TestRead {

    public static void main(String[] args) {
        //读取文件的路径和名称
        String fileName = "F:\\excel\\01.xlsx";
        //调用方法实现读取操作
        EasyExcel.read(fileName,UserData.class,new ExcelLister()).sheet().doRead();
    }
}
