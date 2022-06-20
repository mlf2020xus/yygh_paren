package com.mlf.yygh.order.api;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mlf.yygh.common.result.Result;
import com.mlf.yygh.common.utlis.AuthContextHolder;
import com.mlf.yygh.enums.OrderStatusEnum;
import com.mlf.yygh.model.order.OrderInfo;
import com.mlf.yygh.model.user.UserInfo;
import com.mlf.yygh.order.service.OrderService;
import com.mlf.yygh.vo.order.OrderCountQueryVo;
import com.mlf.yygh.vo.order.OrderQueryVo;
import com.mlf.yygh.vo.user.UserInfoQueryVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by Administrator on 2021/12/6.
 */
@RestController
@RequestMapping("/api/order/orderInfo")
public class OrderApiController {

    @Autowired
    OrderService orderService;

    //生成挂号订单  patientId 就诊人id  scheduleId 排班id
    @PostMapping("auth/submitOrder/{scheduleId}/{patientId}")
    public Result saveOrder(@PathVariable String scheduleId,
                            @PathVariable Long patientId){
       Long orderId = orderService.saveOrder(scheduleId,patientId);
        return Result.ok(orderId);
    }

    //根据订单id查询订单详情
    @GetMapping("auth/getOrders/{orderId}")
    public Result getOrders(@PathVariable String orderId) {
        OrderInfo orderInfo = orderService.getOrder(orderId);
        return Result.ok(orderInfo);
    }

    //订单列表(条件查询带分页)
    @GetMapping("auth/{page}/{limit}")
    public Result list(@PathVariable Long page,
                       @PathVariable Long limit,
                       OrderQueryVo orderQueryVo, HttpServletRequest request) {
        //设置当前用户id
        orderQueryVo.setUserId(AuthContextHolder.getUserId(request));
        Page<OrderInfo> pageParam = new Page<>(page,limit);
        IPage<OrderInfo> pageModel =
                orderService.selectPage(pageParam,orderQueryVo);
        return Result.ok(pageModel);
    }

    @ApiOperation(value = "获取订单状态")
    @GetMapping("auth/getStatusList")
    public Result getStatusList() {
        return Result.ok(OrderStatusEnum.getStatusList());
    }

    @ApiOperation(value = "获取订单统计数据")
    @PostMapping("inner/getCountMap")
    public Map<String, Object> getCountMap(@RequestBody OrderCountQueryVo orderCountQueryVo) {
        return orderService.getCountMap(orderCountQueryVo);
    }
}
