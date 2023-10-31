package com.mei.controller.admin;

import com.github.pagehelper.Page;
import com.mei.dto.OrdersPageQueryDTO;
import com.mei.result.PageResult;
import com.mei.result.Result;
import com.mei.service.OrderService;
import com.mei.vo.OrderStatisticsVO;
import com.mei.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @program: sky-take-out
 * @description:
 * @author: Mr.Ka
 * @create: 2023-10-31 11:24
 **/
@RestController
@RequestMapping("/admin/order")
@Api(tags = "管理端订单模块")
@Slf4j
public class OrderController {
    @Autowired
    private OrderService orderService;

    @ApiOperation("订单搜索功能")
    @GetMapping("/conditionSearch")
    public Result<PageResult> getOrderPage(OrdersPageQueryDTO ordersPageQueryDTO) {
        PageResult result = orderService.getOrderPage(ordersPageQueryDTO);

        return Result.success(result);
    }

    @ApiOperation("查询订单详情")
    @GetMapping("/details/{id}")
    public Result<OrderVO> getOrderDetail(@PathVariable("id") Long orderId) {
        OrderVO orderDetail = orderService.getOrderDetail(orderId);
        return Result.success(orderDetail);
    }

    @ApiOperation("各个状态的订单数量统计")
    @GetMapping("/statistics")
    public Result<OrderStatisticsVO> getOrderStatistics() {
        OrderStatisticsVO data = orderService.getOrderStatistics();

        return Result.success(data);
    }

    @ApiOperation("接单")
    @PutMapping("/confirm")
    public Result receiveOrder(@RequestBody Long id) {
        orderService.receiveOrder(id);

        return Result.success();
    }
}
