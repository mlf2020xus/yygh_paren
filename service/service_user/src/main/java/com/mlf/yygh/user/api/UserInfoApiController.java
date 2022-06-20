package com.mlf.yygh.user.api;

import com.mlf.yygh.common.result.Result;
import com.mlf.yygh.common.utlis.AuthContextHolder;
import com.mlf.yygh.model.user.UserInfo;
import com.mlf.yygh.user.service.UserInfoService;
import com.mlf.yygh.vo.user.LoginVo;
import com.mlf.yygh.vo.user.UserAuthVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by Administrator on 2021/10/6.
 */
@RestController
@RequestMapping("/api/user")
public class UserInfoApiController {

    @Autowired
    private UserInfoService userInfoService;

    //通过手机号登录
    @PostMapping("login")
    public Result login(@RequestBody LoginVo loginVo){
      Map<String,Object> info = userInfoService.userLoginVo(loginVo);
        return Result.ok(info);
    }

    //用户认证接口
    @PostMapping("auth/userAuth")
    public Result userAuth(@RequestBody UserAuthVo userAuthVo, HttpServletRequest request){
        //传递两个参数，第一个参数是用户id,第二个参数认证数据对象
        userInfoService.userAuth(AuthContextHolder.getUserId(request),userAuthVo);
        return Result.ok();
    }

    //根据用户id获取用户信息
    @GetMapping("auth/getUserInfo")
    public Result getUserInfo(HttpServletRequest request){
        Long userId = AuthContextHolder.getUserId(request);
        UserInfo userInfo = userInfoService.getById(userId);
        return Result.ok(userInfo);
    }

}
