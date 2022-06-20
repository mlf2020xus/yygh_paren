package com.mlf.yygh.user.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mlf.yygh.common.result.Result;
import com.mlf.yygh.model.user.UserInfo;
import com.mlf.yygh.user.service.UserInfoService;
import com.mlf.yygh.vo.user.UserInfoQueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Created by Administrator on 2021/12/3.
 */
@RestController
@RequestMapping("/admin/user")
public class UserController {

    @Autowired
    UserInfoService userInfoService;

    //用户列表(条件查询带分页)
    @GetMapping("{page}/{limit}")
    public Result getUserList(@PathVariable Long page,
                              @PathVariable Long limit,
                              UserInfoQueryVo userInfoQueryVo){
        //创建一个page对象
        Page<UserInfo> pageParam = new Page<>(page,limit);
        IPage<UserInfo> pageModel = userInfoService.selectPage(pageParam,userInfoQueryVo);
        return Result.ok(pageModel);
    }

    //用户锁定，只需要根据id改变它的状态
    @GetMapping("lock/{userId}/{status}")
    public Result lock(@PathVariable Long userId, @PathVariable Integer status){
        userInfoService.lock(userId,status);
        return Result.ok();
    }

    //用户详情
    @GetMapping("show/{userId}")
    public Result show(@PathVariable Long userId){
        Map<String,Object> map = userInfoService.show(userId);
        return Result.ok(map);
    }
    //认证审批
    @GetMapping("approval/{userId}/{authStatus}")
    public Result approval(@PathVariable Long userId, @PathVariable Integer authStatus){
        userInfoService.approval(userId,authStatus);
        return Result.ok();
    }

}
