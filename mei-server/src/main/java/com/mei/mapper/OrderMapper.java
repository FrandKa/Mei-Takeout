package com.mei.mapper;

import com.github.pagehelper.Page;
import com.mei.dto.OrdersCancelDTO;
import com.mei.dto.OrdersPageQueryDTO;
import com.mei.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface OrderMapper {
    /**
     * 插入订单数据
     * @param order
     */
    void insert(Orders order);

    /**
     * 根据订单号查询订单
     * @param orderNumber
     */
    @Select("select * from orders where number = #{orderNumber}")
    Orders getByNumber(String orderNumber);

    /**
     * 修改订单信息
     * @param orders
     */
    void update(Orders orders);

    Page<Orders> queryOrderListByUserId(Long userId);

    @Select("SELECT * FROM mei_take_out.orders WHERE id = #{id} ;")
    Orders queryOrderById(Long id);

    void updateStatusById(Long id, Integer status);

    void cancel(OrdersCancelDTO ordersCancelDTO);

    Page<Orders> queryOrderList(OrdersPageQueryDTO ordersPageQueryDTO);

    Integer countStatus(Integer status);

    void rejectByOrderId(Long id, String rejectionReason);

    @Select("SELECT * from mei_take_out.orders WHERE status = #{status}")
    List<Orders> queryOrderByStatus(Integer status);

    @Select("SELECT * from mei_take_out.orders WHERE status = #{status} AND order_time < #{ruleTime}")
    List<Orders> queryByStatusAndOrderTimeLT(Integer status, LocalDateTime ruleTime);

    @Select("SELECT number from mei_take_out.orders WHERE id = #{orderId}")
    String queryOrderNumberById(Long orderId);
}
