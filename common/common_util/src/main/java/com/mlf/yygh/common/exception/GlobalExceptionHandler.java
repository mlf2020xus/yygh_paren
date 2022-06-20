package com.mlf.yygh.common.exception;

import com.mlf.yygh.common.result.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Administrator on 2021/9/29.
 */

//全局异常处理类
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody//以json的形式将返回值返回到页面中
    public Result error(Exception e){
        e.printStackTrace();
        return Result.fail();
    }

    //自定义异常处理
    @ExceptionHandler(YyghException.class)
    @ResponseBody//以json的形式将返回值返回到页面中
    public Result error(YyghException e){
        e.printStackTrace();
        return Result.fail();
    }
}
