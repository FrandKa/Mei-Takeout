package com.mei.service;

import com.mei.dto.DataOverViewQueryDTO;
import com.mei.vo.OrderReportVO;
import com.mei.vo.SalesTop10ReportVO;
import com.mei.vo.TurnoverReportVO;
import com.mei.vo.UserReportVO;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.time.LocalDate;

public interface ReportService {
    TurnoverReportVO turnoverStatistics(LocalDate begin, LocalDate end);

    UserReportVO userStatistics(LocalDate begin, LocalDate end);

    OrderReportVO ordersStatistics(LocalDate begin, LocalDate end);

    SalesTop10ReportVO getTop10(LocalDate begin, LocalDate end);

    void export(HttpServletResponse response);
}
