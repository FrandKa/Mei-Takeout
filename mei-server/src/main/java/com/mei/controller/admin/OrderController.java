package com.mei.controller.admin;

import com.github.pagehelper.Page;
import com.mei.dto.OrdersCancelDTO;
import com.mei.dto.OrdersConfirmDTO;
import com.mei.dto.OrdersPageQueryDTO;
import com.mei.dto.OrdersRejectionDTO;
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
        log.info("搜索订单: {}", ordersPageQueryDTO);
        PageResult result = orderService.getOrderPage(ordersPageQueryDTO);

        return Result.success(result);
    }

    @ApiOperation("查询订单详情")
    @GetMapping("/details/{id}")
    public Result<OrderVO> getOrderDetail(@PathVariable("id") Long orderId) {
        log.info("查看订单详情: {}", orderId);
        OrderVO orderDetail = orderService.getOrderDetail(orderId);
        return Result.success(orderDetail);
    }

    @ApiOperation("各个状态的订单数量统计")
    @GetMapping("/statistics")
    public Result<OrderStatisticsVO> getOrderStatistics() {
        log.info("各个状态的订单数量统计");
        OrderStatisticsVO data = orderService.getOrderStatistics();

        return Result.success(data);
    }

    @ApiOperation("接单")
    @PutMapping("/confirm")
    public Result receiveOrder(@RequestBody OrdersConfirmDTO ordersConfirmDTO) {
        log.info("接单: {}", ordersConfirmDTO);
        orderService.receiveOrder(ordersConfirmDTO);

        return Result.success();
    }

    @ApiOperation("拒单")
    @PutMapping("/rejection")
    public Result rejectOrder(@RequestBody OrdersRejectionDTO ordersRejectionDTO) {
        log.info("拒单: {}", ordersRejectionDTO);
        orderService.rejectOrder(ordersRejectionDTO);

        return Result.success();
    }

    @ApiOperation("取消订单")
    @PutMapping("/cancel")
    public Result cancelOrder(@RequestBody OrdersCancelDTO ordersCancelDTO) {
        log.info("取消订单: {}", ordersCancelDTO);
        orderService.cancel(ordersCancelDTO);

        return Result.success();
    }

    @ApiOperation("完成订单")
    @PutMapping("/complete/{id}")
    public Result completeOrder(@PathVariable("id") Long id) {
        log.info("取消订单: {}", id);
        orderService.completeOrder(id);

        return Result.success();
    }

    @ApiOperation("派送订单")
    @PutMapping("/delivery/{id}")
    public Result deliveryOrder(@PathVariable("id") Long id) {
        log.info("派送订单: {}", id);
        orderService.deliveryOrder(id);

        return Result.success();
    }
}
