package com.mlf.easyexcel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;

/**
 * Created by Administrator on 2021/10/3.
 *
 * Excel的监听器
 */
public class ExcelLister extends AnalysisEventListener<UserData> {

    //一行一行读取excel内容，从第二行开始读取
    @Override
    public void invoke(UserData userData, AnalysisContext analysisContext) {
        System.out.println(userData);
    }

    //读取之后执行
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
