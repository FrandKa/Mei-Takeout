package com.mei.service.impl;

import com.mei.dto.GoodsSalesDTO;
import com.mei.dto.OrderWrapper;
import com.mei.entity.Orders;
import com.mei.mapper.OrderMapper;
import com.mei.mapper.UserMapper;
import com.mei.service.ReportService;
import com.mei.vo.OrderReportVO;
import com.mei.vo.SalesTop10ReportVO;
import com.mei.vo.TurnoverReportVO;
import com.mei.vo.UserReportVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

/**
 * @program: sky-take-out
 * @description:
 * @author: Mr.Ka
 * @create: 2023-11-01 18:03
 **/
@Service
@Slf4j
public class ReportServiceImpl implements ReportService {
    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public TurnoverReportVO turnoverStatistics(LocalDate begin, LocalDate end) {
        List<LocalDate> dataList = new ArrayList<>();
        List<Double> turnoverList = new ArrayList<>();
        TurnoverReportVO vo = new TurnoverReportVO();

        while(!begin.isAfter(end)) {
            dataList.add(begin);
            begin = begin.plusDays(1);
        }

        for (LocalDate date : dataList) {
            LocalDateTime min = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime max = LocalDateTime.of(date, LocalTime.MAX);
            OrderWrapper wrapper = new OrderWrapper();
            wrapper.setMin(min);
            wrapper.setMax(max);
            wrapper.setStatus(Orders.COMPLETED);
            Double sum = orderMapper.sumByTime(wrapper);
            // 注意这里如果没有sum返回值是Null
            if(Objects.isNull(sum)) {
                sum = 0.0;
            }
            turnoverList.add(sum);
        }

        vo.setDateList(StringUtils.join(dataList, ","));
        vo.setTurnoverList(StringUtils.join(turnoverList, ","));
        return vo;
    }

    @Override
    public UserReportVO userStatistics(LocalDate begin, LocalDate end) {
        List<LocalDate> dataList = new ArrayList<>();
        List<Integer> totalUserList = new ArrayList<>();
        List<Integer> newUserList = new ArrayList<>();
        UserReportVO vo = new UserReportVO();
        while(!begin.isAfter(end)) {
            dataList.add(begin);
            Integer sumBefore = userMapper.sumBefore(LocalDateTime.of(begin, LocalTime.MAX));
            totalUserList.add(sumBefore);
            Integer newUser = userMapper.sumBetween(LocalDateTime.of(begin, LocalTime.MIN), LocalDateTime.of(begin, LocalTime.MAX));
            newUserList.add(newUser);
            begin = begin.plusDays(1);
        }
        vo.setDateList(StringUtils.join(dataList, ","));
        vo.setTotalUserList(StringUtils.join(totalUserList, ","));
        vo.setNewUserList(StringUtils.join(newUserList, ","));

        return vo;
    }

    @Override
    public OrderReportVO ordersStatistics(LocalDate begin, LocalDate end) {
        OrderReportVO vo = new OrderReportVO();
        List<LocalDate> dateList = new ArrayList<>();
        List<Integer> orderCountList = new ArrayList<>();
        List<Integer> validOrderCountList = new ArrayList<>();

        while(!begin.isAfter(end)) {
            dateList.add(begin);
            orderCountList.add(getCount(begin, begin, null));
            validOrderCountList.add(getCount(begin, begin, null));
            begin = begin.plusDays(1);
        }

        Integer totalOrderCount = orderCountList.stream().reduce(Integer::sum).get();
        Integer validOrderCount = validOrderCountList.stream().reduce(Integer::sum).get();

        vo.setDateList(StringUtils.join(dateList, ","));
        vo.setOrderCountList(StringUtils.join(orderCountList, ","));
        vo.setTotalOrderCount(totalOrderCount);
        vo.setValidOrderCountList(StringUtils.join(validOrderCountList, ","));
        vo.setValidOrderCount(validOrderCount);
        // 注意不可以除以0
        Double rate = totalOrderCount == 0 ? 0.0 : validOrderCount.doubleValue() / totalOrderCount;
        vo.setOrderCompletionRate(rate);
        log.info("订单数据: {}", vo);

        return vo;
    }

    // 获取订单数 方法抽取:
    private Integer getCount(LocalDate begin, LocalDate end, Integer status) {
        OrderWrapper wrapper = new OrderWrapper();
        wrapper.setMin(LocalDateTime.of(begin, LocalTime.MIN));
        wrapper.setMax(LocalDateTime.of(end, LocalTime.MAX));
        wrapper.setStatus(status);
        return orderMapper.countByTime(wrapper);
    }

    /**
     * top10统计
     * @param begin
     * @param end
     * @return
     */
    @Override
    public SalesTop10ReportVO getTop10(LocalDate begin, LocalDate end) {
        LocalDateTime min = LocalDateTime.of(begin, LocalTime.MIN);
        LocalDateTime max = LocalDateTime.of(end, LocalTime.MAX);
        OrderWrapper wrapper = new OrderWrapper(min, max, Orders.COMPLETED);
        List<GoodsSalesDTO> list = orderMapper.getTopTen(wrapper);
        log.info("top10: {}", list);
        List<String> nameList = list.stream().map(GoodsSalesDTO::getName).toList();
        List<Integer> numberList = list.stream().map(GoodsSalesDTO::getNumber).toList();

        return SalesTop10ReportVO.builder()
                .nameList(StringUtils.join(nameList, ","))
                .numberList(StringUtils.join(numberList, ","))
                .build();
    }
}








