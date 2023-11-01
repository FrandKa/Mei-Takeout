package com.mei.mapper;

import com.mei.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OrderDetailMapper {

    void insertBatch(List<OrderDetail> orderDetailList);

    @Select("SELECT * FROM mei_take_out.order_detail WHERE order_id = #{orderId}")
    List<OrderDetail> queryByOrderId(Long orderId);
}
