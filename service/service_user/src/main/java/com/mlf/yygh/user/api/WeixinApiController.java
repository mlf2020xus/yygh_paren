package com.mlf.yygh.user.api;

import com.alibaba.fastjson.JSONObject;
import com.mlf.yygh.common.exception.YyghException;
import com.mlf.yygh.common.helper.JwtHelper;
import com.mlf.yygh.common.result.Result;
import com.mlf.yygh.common.result.ResultCodeEnum;
import com.mlf.yygh.model.user.UserInfo;
import com.mlf.yygh.user.service.UserInfoService;
import com.mlf.yygh.user.utils.ConstantPropertiesUtil;
import com.mlf.yygh.user.utils.HttpClientUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2021/10/7.
 */

//进行编写微信登录的两个接口
//1、生成微信扫描二维码
//2、编写回调的方法，获取扫描人信息

//操作微信二维码登录的接口
@Controller   //单独使用@Controller  可以完成页面的跳转；而@RestController是返回json数据，不能实现跳转功能
@RequestMapping("/api/ucenter/wx")
public class WeixinApiController {

    @Autowired
    private UserInfoService userInfoService;

    /** 微信扫描后回调的接口 **/
    @GetMapping("callback")
    public String callback(String code, String state){
        //1、获取授权临时票据code
        System.out.println("微信授权服务器回调。。。。。。");
        System.out.println("code:"+code);
        System.out.println("state:"+ state);
        StringBuffer baseAccessTokenUrl = new StringBuffer()
                .append("https://api.weixin.qq.com/sns/oauth2/access_token")
                .append("?appid=%s")
                .append("&secret=%s")
                .append("&code=%s")
                .append("&grant_type=authorization_code");
        //2、拿着微信code和微信id和密钥，请求微信固定地址，得到两个值access_token、openid
        //使用code和appid以及appscrect换取access_token
        String accessTokenUrl = String.format(baseAccessTokenUrl.toString(),
                ConstantPropertiesUtil.WX_OPEN_APP_ID,//id
                ConstantPropertiesUtil.WX_OPEN_APP_SECRET,//密钥
                code);//code要按照顺序
        try {
            //使用httpclient请求该地址accessTokenUrl
            //result里面有access_token和openid
            String result = HttpClientUtils.get(accessTokenUrl);
            //从返回的字符串里获取这两个值access_token，openid；截取字符串或者json转换两种方法
            JSONObject resultJson = JSONObject.parseObject(result);
            String accessToken = resultJson.getString("access_token");
            String openId = resultJson.getString("openid");
            //判断数据库中是否存在微信的是扫码人的信息，根据openid做判断
           UserInfo userInfo = userInfoService.selectWxInfoOpenId(openId);
            if(userInfo == null){//数据库不存在微信信息
                //3、拿着这两个值access_token，openid请求微信地址，得到扫描人信息
                String baseUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo" +
                        "?access_token=%s" +
                        "&openid=%s";
                String userInfoUrl = String.format(baseUserInfoUrl, accessToken, openId);
                //resultInfo就是扫码人的信息
                String resultInfo = HttpClientUtils.get(userInfoUrl);
                //字符串转成json来获取扫码人的信息
                JSONObject resultUserInfoJson = JSONObject.parseObject(resultInfo);
                //解析用户信息
                String nickname = resultUserInfoJson.getString("nickname");//扫码人昵称
                String headimgurl = resultUserInfoJson.getString("headimgurl");//扫码人头像
                //扫码人信息入库：
                userInfo = new UserInfo();
                userInfo.setOpenid(openId);
                userInfo.setNickName(nickname);
                userInfo.setStatus(1);
                userInfoService.save(userInfo);
            }
            //手机登录返回了name和JWT生成的token*****************************
            Map<String, Object> map = new HashMap<>();
            String name = userInfo.getName();
            if(StringUtils.isEmpty(name)) {
                name = userInfo.getNickName();
            }
            if(StringUtils.isEmpty(name)) {
                name = userInfo.getPhone();
            }
            map.put("name", name);
            /** 手机号如果为空，返回openid；手机号不为空，返回返回openid值是空字符串 **/
            //前端判断，openid不为空，绑定手机号；openid为空，说明已经绑定手机号了
            if(StringUtils.isEmpty(userInfo.getPhone())) {
                map.put("openid", userInfo.getOpenid());
            } else {
                map.put("openid", "");
            }
            //使用JWT生成一个字符串token
            String token = JwtHelper.createToken(userInfo.getId(), name);
            map.put("token", token);
            //注意：weixin/callback需要在前端的pages下创建一个weixin文件夹，
            // 再在weixin文件夹下创建callback文件。这是跳转的规范
            //跳转到前端页面中  redirect表示重定向
            return "redirect:" + ConstantPropertiesUtil.YYGH_BASE_URL
                    + "/weixin/callback?token="+map.get("token")
                    +"&openid="+map.get("openid")
                    +"&name="+URLEncoder.encode((String) map.get("name"),"utf-8");
        } catch (Exception e) {
            throw new YyghException(ResultCodeEnum.FETCH_ACCESSTOKEN_FAILD);
        }
    }
    /**
     * 获取微信登录参数
     */
    @GetMapping("getLoginParam")
    @ResponseBody
    public Result genQrConnect() {
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("appid", ConstantPropertiesUtil.WX_OPEN_APP_ID);//返回的第一个参数
            map.put("scope", "snsapi_login");//固定写法，value不可改变
            //重定向地址需要进行编码
            String redirectUri = URLEncoder.encode(ConstantPropertiesUtil.WX_OPEN_REDIRECT_URL, "UTF-8");
                map.put("redirectUri", redirectUri);//重定向地址
            map.put("state", System.currentTimeMillis() + "");//这个参数可有可无
            return Result.ok(map);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
