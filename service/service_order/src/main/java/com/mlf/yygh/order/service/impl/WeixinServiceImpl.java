package com.mlf.yygh.order.service.impl;

import com.github.wxpay.sdk.WXPayUtil;
import com.mlf.yygh.enums.PaymentTypeEnum;
import com.mlf.yygh.model.order.OrderInfo;
import com.mlf.yygh.order.service.OrderService;
import com.mlf.yygh.order.service.PaymentService;
import com.mlf.yygh.order.service.WeixinService;
import com.mlf.yygh.order.utils.ConstantPropertiesUtils;
import com.mlf.yygh.order.utils.HttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2021/12/8.
 */
@Service
public class WeixinServiceImpl implements WeixinService {

    @Autowired
    private OrderService orderService;
    @Autowired
    private PaymentService paymentService;

    //通过它设置支付的有效时间
    @Autowired
    private RedisTemplate redisTemplate;


    //下单 生成微信支付二维码  orderId  订单id
    @Override
    public Map createNative(Long orderId) {
        try {
            //从redis获取数据
            Map payMap = (Map) redisTemplate.opsForValue().get(orderId.toString());
            if(null != payMap) return payMap;
            //1、根据id获取订单信息
            OrderInfo order = orderService.getById(orderId);
            // 2、保存交易记录   向支付记录表添加信息  PaymentTypeEnum.WEIXIN.getStatus()表示支付的状态
            paymentService.savePaymentInfo(order, PaymentTypeEnum.WEIXIN.getStatus());
            //3、设置参数  目的为了调用微信生成二维码接口
            //把微信转换成xml格式，使用商户key进行加密  若不这样做微信那边是不认识的
            Map paramMap = new HashMap();
            paramMap.put("appid", ConstantPropertiesUtils.APPID);//公众号
            paramMap.put("mch_id", ConstantPropertiesUtils.PARTNER);//商户号
            paramMap.put("nonce_str", WXPayUtil.generateNonceStr());//使用微信那边的工具类，生成的随机的字符串
            String body = order.getReserveDate() + "就诊"+ order.getDepname();
            paramMap.put("body", body);
            paramMap.put("out_trade_no", order.getOutTradeNo());//订单编号
           // paramMap.put("total_fee", order.getAmount().multiply(new BigDecimal("100")).longValue()+"");  //订单金额
            paramMap.put("total_fee", "1");//1   表示0.01元  一分钱  为了测试统一写这个值
            paramMap.put("spbill_create_ip", "127.0.0.1");
            //回调地址  暂且没有用到
            paramMap.put("notify_url", "http://guli.shop/api/order/weixinPay/weixinNotify");
            //支付类型
            paramMap.put("trade_type", "NATIVE");
            //4、HTTPClient来根据URL访问第三方接口并且传递参数
            //调用微信生成二维码接口 HttpClient调用  https://api.mch.weixin.qq.com/pay/unifiedorder固定值
            HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");
            //client设置参数   设置map参数  将map集合里面的数据转换成xml格式  使用微信提供的工具类即可转换
            //第二个参数是加密的key  指的是获取配置文件里面的商户key
            client.setXmlParam(WXPayUtil.generateSignedXml(paramMap, ConstantPropertiesUtils.PARTNERKEY));
            //支持https的形式  默认是http
            client.setHttps(true);
            client.post();
            //5、有微信那边返回第三方的数据  返回的xml格式，上传是以xml格式上传，返回的自然也是xml（需要转换成map集合形式）
            String xml = client.getContent();
            Map<String, String> resultMap = WXPayUtil.xmlToMap(xml);
            System.out.print("resultMap"+resultMap);//方便测试查看
            //4、封装返回结果集
            Map map = new HashMap<>();
            map.put("orderId", orderId);//订单id
            map.put("totalFee", order.getAmount());//订单金额
            map.put("resultCode", resultMap.get("result_code"));//状态码
            map.put("codeUrl", resultMap.get("code_url"));//微信支付二维码地址
            if(null != resultMap.get("result_code")) {
                //微信支付二维码2小时过期，可采取2小时未支付取消订单  参数分别表示K、V、时间、单位
                redisTemplate.opsForValue().set(orderId.toString(), map, 120, TimeUnit.MINUTES);
            }
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    //调用微信接口实现支付状态查询
    @Override
    public Map<String, String> queryPayStatus(Long orderId, String name) {
        try {
            //1、根据orderId获取订单信息
            OrderInfo orderInfo = orderService.getById(orderId);
            //2、封装提交的参数
            Map paramMap = new HashMap<>();
            paramMap.put("appid", ConstantPropertiesUtils.APPID);//微信支付id
            paramMap.put("mch_id", ConstantPropertiesUtils.PARTNER);//商户号
            paramMap.put("out_trade_no", orderInfo.getOutTradeNo());//订单编号
            paramMap.put("nonce_str", WXPayUtil.generateNonceStr());//随机生成的字符串
            //3、设置请求内容
            HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/orderquery");
            //map集合转成xml格式   商户的加密的key
            client.setXmlParam(WXPayUtil.generateSignedXml(paramMap, ConstantPropertiesUtils.PARTNERKEY));
            client.setHttps(true);
            client.post();
            //4、返回第三方的数据，转成Map
            String xml = client.getContent();
            Map<String, String> resultMap = WXPayUtil.xmlToMap(xml);
            System.out.println("支付状态resultMap:"+resultMap);
            //5、把接口的数据返回
            return resultMap;
        } catch (Exception e) {
            return null;
        }
    }
}
