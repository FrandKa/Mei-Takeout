package com.mei.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mei.constant.MessageConstant;
import com.mei.context.BaseContext;
import com.mei.dto.*;
import com.mei.entity.*;
import com.mei.exception.AddressBookBusinessException;
import com.mei.exception.OrderBusinessException;
import com.mei.exception.ShoppingCartBusinessException;
import com.mei.mapper.*;
import com.mei.result.PageResult;
import com.mei.service.OrderService;
import com.mei.utils.BaiduPositionUtil;
import com.mei.utils.WeChatPayUtil;
import com.mei.vo.OrderPaymentVO;
import com.mei.vo.OrderStatisticsVO;
import com.mei.vo.OrderSubmitVO;
import com.mei.vo.OrderVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 订单
 */
@Service
@Slf4j
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private AddressBookMapper addressBookMapper;
    @Autowired
    private WeChatPayUtil weChatPayUtil;
    @Autowired
    private BaiduPositionUtil baiduPositionUtil;

    /**
     * 用户下单
     *
     * @param ordersSubmitDTO
     * @return
     */
    @Transactional
    public OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO) {
        //异常情况的处理（收货地址为空、购物车为空）
        AddressBook addressBook = addressBookMapper.getById(ordersSubmitDTO.getAddressBookId());
        if (addressBook == null) {
            throw new AddressBookBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);
        }

        Long userId = BaseContext.getCurrentId();
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUserId(userId);

        //查询当前用户的购物车数据
        List<ShoppingCart> shoppingCartList = shoppingCartMapper.list(shoppingCart);
        if (shoppingCartList == null || shoppingCartList.size() == 0) {
            throw new ShoppingCartBusinessException(MessageConstant.SHOPPING_CART_IS_NULL);
        }

        //构造订单数据
        Orders order = new Orders();
        BeanUtils.copyProperties(ordersSubmitDTO,order);
        order.setPhone(addressBook.getPhone());
        order.setAddress(addressBook.getDetail());
        order.setConsignee(addressBook.getConsignee());
        order.setNumber(String.valueOf(System.currentTimeMillis()));
        order.setUserId(userId);
        order.setStatus(Orders.PENDING_PAYMENT);
        order.setPayStatus(Orders.UN_PAID);
        order.setOrderTime(LocalDateTime.now());

        baiduPositionUtil.checkOutOfRange(order.getAddress());

        //向订单表插入1条数据
        orderMapper.insert(order);

        //订单明细数据
        List<OrderDetail> orderDetailList = new ArrayList<>();
        for (ShoppingCart cart : shoppingCartList) {
            OrderDetail orderDetail = new OrderDetail();
            BeanUtils.copyProperties(cart, orderDetail);
            orderDetail.setOrderId(order.getId());
            orderDetailList.add(orderDetail);
        }

        //向明细表插入n条数据
        orderDetailMapper.insertBatch(orderDetailList);

        //清理购物车中的数据
        shoppingCartMapper.deleteAllByUserId(userId);
        log.info("清空: {}", userId);

        //封装返回结果
        OrderSubmitVO orderSubmitVO = OrderSubmitVO.builder()
                .id(order.getId())
                .orderNumber(order.getNumber())
                .orderAmount(order.getAmount())
                .orderTime(order.getOrderTime())
                .build();

        return orderSubmitVO;
    }

    /**
     * 订单支付
     *
     * @param ordersPaymentDTO
     * @return
     */
    public OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        // 当前登录用户id
        Long userId = BaseContext.getCurrentId();
        User user = userMapper.queryById(userId);
        String orderNumber = ordersPaymentDTO.getOrderNumber();
        Integer payMethod = ordersPaymentDTO.getPayMethod();
        Orders orders = orderMapper.getByNumber(orderNumber);
        orders.setCheckoutTime(LocalDateTime.now());
        orders.setStatus(Orders.REFUND);
        orders.setPayStatus(Orders.PAID);

        orderMapper.update(orders);

        return null;
    }

    @Override
    public PageResult getHistoryOrders(OrdersPageQueryDTO ordersPageQueryDTO) {
        int pageNumber = ordersPageQueryDTO.getPage();
        int pageSize = ordersPageQueryDTO.getPageSize();
        PageHelper.startPage(pageNumber, pageSize);
        Long userId = BaseContext.getCurrentId();
        Page<Orders> page = orderMapper.queryOrderListByUserId(userId);
        long total = page.getTotal();
        List<Orders> list = page.getResult();
        if(Objects.isNull(list) || list.isEmpty()) {
            return new PageResult(0L, new ArrayList());
        }
        List<OrderVO> data = new ArrayList<>();
        for (Orders orders : list) {
            OrderVO orderVO = new OrderVO();
            BeanUtils.copyProperties(orders, orderVO);
            Long orderId = orders.getId();
            List<OrderDetail> detailList = orderDetailMapper.queryByOrderId(orderId);
            orderVO.setOrderDetailList(detailList);
            data.add(orderVO);
        }
        log.info("查询到用户: {} 的历史订单数据: {}", userId, list);
        return new PageResult(total, data);
    }

    @Override
    public OrderVO getOrderDetail(Long orderId) {
        Orders orders = orderMapper.queryOrderById(orderId);
        List<OrderDetail> detailList = orderDetailMapper.queryByOrderId(orderId);
        log.info("查询id: {} 订单详情: {}", orderId, detailList);
        OrderVO orderVO = new OrderVO();
        BeanUtils.copyProperties(orders, orderVO);
        orderVO.setOrderDetailList(detailList);

        return orderVO;
    }

    @Override
    public void cancel(OrdersCancelDTO ordersCancelDTO) {
        orderMapper.cancel(ordersCancelDTO);
    }

    // 管理端获取订单信息:
    @Override
    public PageResult getOrderPage(OrdersPageQueryDTO ordersPageQueryDTO) {
        int pageNumber = ordersPageQueryDTO.getPage();
        int pageSize = ordersPageQueryDTO.getPageSize();
        PageHelper.startPage(pageNumber, pageSize);
        Page<Orders> page = orderMapper.queryOrderList(ordersPageQueryDTO);
        long total = page.getTotal();
        List<Orders> list = page.getResult();
        if(Objects.isNull(list) || list.isEmpty()) {
            return new PageResult(0L, new ArrayList());
        }
        List<OrderVO> data = new ArrayList<>();
        for (Orders orders : list) {
            OrderVO orderVO = new OrderVO();
            BeanUtils.copyProperties(orders, orderVO);
            Long orderId = orders.getId();
            List<OrderDetail> detailList = orderDetailMapper.queryByOrderId(orderId);
            orderVO.setOrderDetailList(detailList);
            data.add(orderVO);
        }
        return new PageResult(total, data);
    }

    @Override
    public OrderStatisticsVO getOrderStatistics() {
        OrderStatisticsVO orderStatisticsVO = new OrderStatisticsVO();
        orderStatisticsVO.setConfirmed(orderMapper.countStatus(Orders.CONFIRMED));
        orderStatisticsVO.setDeliveryInProgress(orderMapper.countStatus(Orders.DELIVERY_IN_PROGRESS));
        orderStatisticsVO.setToBeConfirmed(orderMapper.countStatus(Orders.TO_BE_CONFIRMED));
        return orderStatisticsVO;
    }

    @Override
    public void receiveOrder(OrdersConfirmDTO ordersConfirmDTO) {
        // 将status改为3;
        orderMapper.updateStatusById(ordersConfirmDTO.getId(), Orders.CONFIRMED);
    }

    @Override
    public void rejectOrder(OrdersRejectionDTO ordersRejectionDTO) {
        Long orderId = ordersRejectionDTO.getId();
        String rejectionReason = ordersRejectionDTO.getRejectionReason();
        orderMapper.rejectByOrderId(orderId, rejectionReason);
    }

    @Override
    public void completeOrder(Long id) {
        orderMapper.updateStatusById(id, Orders.COMPLETED);
    }

    @Override
    public void deliveryOrder(Long id) {
        orderMapper.updateStatusById(id, Orders.DELIVERY_IN_PROGRESS);
    }
}
