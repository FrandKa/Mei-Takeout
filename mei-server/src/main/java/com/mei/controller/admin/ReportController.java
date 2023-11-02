package com.mei.controller.admin;

import com.mei.dto.DataOverViewQueryDTO;
import com.mei.result.Result;
import com.mei.service.ReportService;
import com.mei.vo.OrderReportVO;
import com.mei.vo.SalesTop10ReportVO;
import com.mei.vo.TurnoverReportVO;
import com.mei.vo.UserReportVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileUrlResource;
import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;

/**
 * @program: sky-take-out
 * @description:
 * @author: Mr.Ka
 * @create: 2023-11-01 18:01
 **/

@RestController
@RequestMapping("/admin/report")
@Api(tags = "数据统计")
@Slf4j
public class ReportController {
    @Autowired
    private ReportService reportService;

    /**
     * 营业额数据
     * @param begin
     * @param end
     * @return
     */
    @ApiOperation("营业额数据")
    @GetMapping("/turnoverStatistics")
    public Result<TurnoverReportVO> turnoverStatistics(
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end
            ) {
        log.info("查询营业额度: {} - {}", begin, end);
        TurnoverReportVO vo = reportService.turnoverStatistics(begin, end);

        return Result.success(vo);
    }

    /**
     * 用户数据
     * @param begin
     * @param end
     * @return
     */
    @ApiOperation("用户数据")
    @GetMapping("/userStatistics")
    public Result<UserReportVO> userStatistics(
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end
    ) {
        log.info("查询用户数据: {} - {}", begin, end);
        UserReportVO vo = reportService.userStatistics(begin, end);

        return Result.success(vo);
    }

    /**
     * 订单数据
     * @param begin
     * @param end
     * @return
     */
    @ApiOperation("订单数据")
    @GetMapping("/ordersStatistics")
    public Result<OrderReportVO> ordersStatistics(
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end
    ) {
        log.info("查询订单数据: {} - {}", begin, end);
        OrderReportVO vo = reportService.ordersStatistics(begin, end);

        return Result.success(vo);
    }

    /**
     * 销量前十数据
     * @param begin
     * @param end
     * @return
     */
    @ApiOperation("销量前十数据")
    @GetMapping("/top10")
    public Result<SalesTop10ReportVO> getTop10(
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end
    ) {
        SalesTop10ReportVO vo = reportService.getTop10(begin, end);

        return Result.success(vo);
    }

    /**
     * 导出报表
     */
    @ApiOperation("数据导出为excel")
    @GetMapping("/export")
    public void export(HttpServletResponse response) {
        // 导出最近30天的
        log.info("数据导出");
        reportService.export(response);
    }
}












