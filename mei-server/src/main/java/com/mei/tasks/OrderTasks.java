package com.mei.tasks;

import com.mei.dto.OrdersCancelDTO;
import com.mei.entity.Orders;
import com.mei.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

/**
 * @program: sky-take-out
 * @description:
 * @author: Mr.Ka
 * @create: 2023-11-01 13:40
 **/
@Component
@Slf4j
public class OrderTasks {
    @Autowired
    private OrderMapper orderMapper;

    // TODO KA 2023/11/1 14:06 设置取消这里读取数据库操作的日志打印
    @Scheduled(cron = "* 0/1 * * * ?")
    @Async
    public void cancelOverTime() {
        LocalDateTime ruleTime = LocalDateTime.now().plusMinutes(-15);
        List<Orders> list = orderMapper.queryByStatusAndOrderTimeLT(Orders.PENDING_PAYMENT, ruleTime);
        if(Objects.isNull(list) || list.isEmpty()) {
            return;
        }
        for (Orders orders : list) {
            log.info("超时订单取消: {}", orders);
            OrdersCancelDTO ordersCancelDTO = new OrdersCancelDTO();
            ordersCancelDTO.setId(orders.getId());
            ordersCancelDTO.setCancelReason("订单支付超时");
            orderMapper.cancel(ordersCancelDTO);
        }
    }

    @Scheduled(cron = "0 0 1 * * ?")
    @Async
    public void processDeliverOrder() {
        LocalDateTime dateTime = LocalDateTime.now().plusHours(-1);
        List<Orders> list = orderMapper.queryByStatusAndOrderTimeLT(Orders.DELIVERY_IN_PROGRESS, dateTime);
        for (Orders orders : list) {
            log.info("超时派送中订单处理: {}", orders);
            orderMapper.updateStatusById(orders.getId(), Orders.COMPLETED);
        }
    }
}
