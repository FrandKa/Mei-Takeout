package com.mei.service;


import com.mei.dto.*;
import com.mei.entity.OrderDetail;
import com.mei.result.PageResult;
import com.mei.vo.OrderPaymentVO;
import com.mei.vo.OrderStatisticsVO;
import com.mei.vo.OrderSubmitVO;
import com.mei.vo.OrderVO;

import java.util.List;

public interface OrderService {

    /**
     * 用户下单
     * @param ordersSubmitDTO
     * @return
     */
    OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO);

    /**
     * 订单支付
     * @param ordersPaymentDTO
     * @return
     */
    OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception;

    /**
     * 支付成功，修改订单状态
     * @param outTradeNo
     */
    void paySuccess(String outTradeNo);

    PageResult getHistoryOrders(OrdersPageQueryDTO ordersPageQueryDTO);

    OrderVO getOrderDetail(Long orderId);

    void cancel(OrdersCancelDTO ordersCancelDTO);

    PageResult getOrderPage(OrdersPageQueryDTO ordersPageQueryDTO);

    OrderStatisticsVO getOrderStatistics();

    void receiveOrder(OrdersConfirmDTO ordersConfirmDTO);

    void rejectOrder(OrdersRejectionDTO ordersRejectionDTO);

    void completeOrder(Long id);

    void deliveryOrder(Long id);
}
