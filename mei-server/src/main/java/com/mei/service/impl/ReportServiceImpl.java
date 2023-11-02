package com.mei.service.impl;

import com.mei.dto.GoodsSalesDTO;
import com.mei.dto.OrderWrapper;
import com.mei.entity.Orders;
import com.mei.mapper.OrderMapper;
import com.mei.mapper.UserMapper;
import com.mei.service.ReportService;
import com.mei.service.WorkspaceService;
import com.mei.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
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

    @Autowired
    private WorkspaceService workspaceService;

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

    /**
     * 导出数据报表
     * @param response
     */
    @Override
    @Transactional
    public void export(HttpServletResponse response) {
        try {
            InputStream ips = this.getClass().getClassLoader().getResourceAsStream("export/export.xlsx");
            XSSFWorkbook excel = new XSSFWorkbook(ips);
            XSSFSheet sheet1 = excel.getSheet("Sheet1");
            // 准备时间(30天)
            LocalDate begin = LocalDate.now().minusDays(30);
            LocalDate end = LocalDate.now().minusDays(1);
            LocalDateTime max = LocalDateTime.of(begin, LocalTime.MAX);
            LocalDateTime min = LocalDateTime.of(begin, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(end, LocalTime.MAX);
            // 设置时间
            sheet1.getRow(1).getCell(1).setCellValue("时间: " + begin + " 至 " + end);
            // 获取概览数据:
            BusinessDataVO totalData = workspaceService.getBusinessData(min, endTime);
            XSSFRow r4 = sheet1.getRow(3);
            r4.getCell(2).setCellValue(totalData.getTurnover());
            r4.getCell(4).setCellValue(totalData.getOrderCompletionRate());
            r4.getCell(6).setCellValue(totalData.getNewUsers());
            XSSFRow r5 = sheet1.getRow(4);
            r5.getCell(2).setCellValue(totalData.getValidOrderCount());
            r5.getCell(4).setCellValue(totalData.getUnitPrice());

            // 获取明细数据:
            int row = 7;

            while(!min.isAfter(endTime)) {
                BusinessDataVO businessData = workspaceService.getBusinessData(min, max);
                setDetailData(sheet1.getRow(row), begin, businessData);
                // 下一天:
                row += 1;
                begin = begin.plusDays(1);
                min = min.plusDays(1);
                max = max.plusDays(1);
            }

            ServletOutputStream servletOutputStream = response.getOutputStream();
            excel.write(servletOutputStream);
            servletOutputStream.close();
            excel.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 设置指定行的数据
     * @param row
     * @param date
     * @param data
     */
    private void setDetailData(XSSFRow row, LocalDate date, BusinessDataVO data) {
        row.getCell(1).setCellValue(String.valueOf(date));
        row.getCell(2).setCellValue(String.valueOf(data.getTurnover()));
        row.getCell(3).setCellValue(String.valueOf(data.getValidOrderCount()));
        row.getCell(4).setCellValue(String.valueOf(data.getOrderCompletionRate()));
        row.getCell(5).setCellValue(String.valueOf(data.getUnitPrice()));
        row.getCell(6).setCellValue(String.valueOf(data.getNewUsers()));
    }
}








