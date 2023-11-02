package com.mei.service.impl;

import com.mei.constant.StatusConstant;
import com.mei.dto.OrderWrapper;
import com.mei.entity.Orders;
import com.mei.mapper.DishMapper;
import com.mei.mapper.OrderMapper;
import com.mei.mapper.SetMealMapper;
import com.mei.mapper.UserMapper;
import com.mei.service.WorkspaceService;
import com.mei.vo.BusinessDataVO;
import com.mei.vo.DishOverViewVO;
import com.mei.vo.OrderOverViewVO;
import com.mei.vo.SetmealOverViewVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class WorkspaceServiceImpl implements WorkspaceService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetMealMapper setmealMapper;

    /**
     * 根据时间段统计营业数据
     * @param begin
     * @param end
     * @return
     */
    public BusinessDataVO getBusinessData(LocalDateTime begin, LocalDateTime end) {
        /**
         * 营业额：当日已完成订单的总金额
         * 有效订单：当日已完成订单的数量
         * 订单完成率：有效订单数 / 总订单数
         * 平均客单价：营业额 / 有效订单数
         * 新增用户：当日新增用户的数量
         */
        OrderWrapper wrapper = new OrderWrapper();
        wrapper.setMin(begin);
        wrapper.setMax(end);

        //查询总订单数
        Integer totalOrderCount = orderMapper.countByTime(wrapper);

        wrapper.setStatus(Orders.COMPLETED);
        //营业额
        Double turnover = orderMapper.sumByTime(wrapper);
        turnover = turnover == null? 0.0 : turnover;

        //有效订单数
        Integer validOrderCount = orderMapper.countByTime(wrapper);

        Double unitPrice = 0.0;

        Double orderCompletionRate = 0.0;
        if(totalOrderCount != 0 && validOrderCount != 0){
            //订单完成率
            orderCompletionRate = validOrderCount.doubleValue() / totalOrderCount;
            //平均客单价
            unitPrice = turnover / validOrderCount;
        }

        //新增用户数
        Integer newUsers = userMapper.sumBetween(begin, end);

        return BusinessDataVO.builder()
                .turnover(turnover)
                .validOrderCount(validOrderCount)
                .orderCompletionRate(orderCompletionRate)
                .unitPrice(unitPrice)
                .newUsers(newUsers)
                .build();
    }


    /**
     * 查询订单管理数据
     *
     * @return
     */
    public OrderOverViewVO getOrderOverView() {
        OrderWrapper wrapper = new OrderWrapper();
        wrapper.setMin(LocalDateTime.now().with(LocalTime.MIN));
        wrapper.setStatus(Orders.TO_BE_CONFIRMED);

        //待接单
        Integer waitingOrders = orderMapper.countByTime(wrapper);

        //待派送
        wrapper.setStatus(Orders.CONFIRMED);
        Integer deliveredOrders = orderMapper.countByTime(wrapper);

        //已完成
        wrapper.setStatus(Orders.COMPLETED);
        Integer completedOrders = orderMapper.countByTime(wrapper);

        //已取消
        wrapper.setStatus(Orders.CANCELLED);

        Integer cancelledOrders = orderMapper.countByTime(wrapper);

        //全部订单
        wrapper.setStatus(null);

        Integer allOrders = orderMapper.countByTime(wrapper);

        return OrderOverViewVO.builder()
                .waitingOrders(waitingOrders)
                .deliveredOrders(deliveredOrders)
                .completedOrders(completedOrders)
                .cancelledOrders(cancelledOrders)
                .allOrders(allOrders)
                .build();
    }

    /**
     * 查询菜品总览
     *
     * @return
     */
    public DishOverViewVO getDishOverView() {
        Map map = new HashMap();
        map.put("status", StatusConstant.ENABLE);
        Integer sold = dishMapper.countByMap(map);

        map.put("status", StatusConstant.DISABLE);
        Integer discontinued = dishMapper.countByMap(map);

        return DishOverViewVO.builder()
                .sold(sold)
                .discontinued(discontinued)
                .build();
    }

    /**
     * 查询套餐总览
     *
     * @return
     */
    public SetmealOverViewVO getSetmealOverView() {
        Map map = new HashMap();
        map.put("status", StatusConstant.ENABLE);
        Integer sold = setmealMapper.countByMap(map);

        map.put("status", StatusConstant.DISABLE);
        Integer discontinued = setmealMapper.countByMap(map);

        return SetmealOverViewVO.builder()
                .sold(sold)
                .discontinued(discontinued)
                .build();
    }
}
