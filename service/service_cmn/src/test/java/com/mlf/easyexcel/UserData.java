package com.mlf.easyexcel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * Created by Administrator on 2021/10/3.
 */
@Data
public class UserData {

    @ExcelProperty("用户编号")
    private int uid;
    @ExcelProperty("用户名称")
    private String username;
}
