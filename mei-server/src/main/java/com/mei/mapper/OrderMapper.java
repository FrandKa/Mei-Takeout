package com.mei.mapper;

import com.github.pagehelper.Page;
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
}
