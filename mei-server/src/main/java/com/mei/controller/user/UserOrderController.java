package com.mei.controller.user;

import com.mei.dto.OrdersPageQueryDTO;
import com.mei.dto.OrdersPaymentDTO;
import com.mei.dto.OrdersSubmitDTO;
import com.mei.dto.ShoppingCartDTO;
import com.mei.entity.OrderDetail;
import com.mei.result.PageResult;
import com.mei.result.Result;
import com.mei.service.OrderService;
import com.mei.service.UserShoppingCartService;
import com.mei.vo.OrderPaymentVO;
import com.mei.vo.OrderSubmitVO;
import com.mei.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 订单
 */
@RestController("userOrderController")
@RequestMapping("/user/order")
@Slf4j
@Api(tags = "C端订单接口")
public class UserOrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserShoppingCartService shoppingCartService;

    /**
     * 用户下单
     *
     * @param ordersSubmitDTO
     * @return
     */
    @PostMapping("/submit")
    @ApiOperation("用户下单")
    public Result<OrderSubmitVO> submit(@RequestBody OrdersSubmitDTO ordersSubmitDTO) {
        log.info("用户下单：{}", ordersSubmitDTO);
        OrderSubmitVO orderSubmitVO = orderService.submitOrder(ordersSubmitDTO);
        return Result.success(orderSubmitVO);
    }

    /**
     * 订单支付
     *
     * @param ordersPaymentDTO
     * @return
     */
    @PutMapping("/payment")
    @ApiOperation("订单支付")
    public Result<OrderPaymentVO> payment(@RequestBody OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        log.info("订单支付：{}", ordersPaymentDTO);
        OrderPaymentVO orderPaymentVO = orderService.payment(ordersPaymentDTO);
        log.info("生成预支付交易单：{}", orderPaymentVO);
        return Result.success(orderPaymentVO);
    }

    @ApiOperation("历史订单查看")
    @GetMapping("/historyOrders")
    public Result<PageResult> getHistoryOrders(OrdersPageQueryDTO ordersPageQueryDTO) {
        log.info("查询历史订单数据: {}", ordersPageQueryDTO);
        PageResult result = orderService.getHistoryOrders(ordersPageQueryDTO);

        return Result.success(result);
    }

    @ApiOperation("查询订单详情")
    @GetMapping("/orderDetail/{id}")
    public Result<OrderVO> getOrderDetail(@PathVariable("id") Long orderId) {
        OrderVO data = orderService.getOrderDetail(orderId);

        return Result.success(data);
    }

    @ApiOperation("再来一单")
    @PostMapping("/repetition/{id}")
    public Result repetition(@PathVariable("id") Long orderId) {
        // 根据orderId获取详情信息:
        OrderVO orderDetail = orderService.getOrderDetail(orderId);
        List<OrderDetail> orderDetailList = orderDetail.getOrderDetailList();
        for (OrderDetail detail : orderDetailList) {
            Long dishId = detail.getDishId();
            String dishFlavor = detail.getDishFlavor();
            Long setmealId = detail.getSetmealId();
            // 创建ShopCart对象, 填充数据
            ShoppingCartDTO shoppingCartDTO = new ShoppingCartDTO();
            shoppingCartDTO.setDishId(dishId);
            shoppingCartDTO.setSetmealId(setmealId);
            shoppingCartDTO.setDishFlavor(dishFlavor);
            // 添加到购物车
            shoppingCartService.add(shoppingCartDTO);
        }

        return Result.success();
    }

    @ApiOperation("取消订单")
    @PutMapping("/cancel/{id}")
    public Result cancel(@PathVariable("id") Long orderId) {
        orderService.cancel(orderId);

        return Result.success();
    }

}
