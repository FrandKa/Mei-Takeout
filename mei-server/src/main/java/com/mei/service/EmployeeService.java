package com.mei.service;

import com.mei.dto.EmployeeDTO;
import com.mei.dto.EmployeeLoginDTO;
import com.mei.dto.EmployeePageQueryDTO;
import com.mei.entity.Employee;
import com.mei.result.PageResult;
import org.springframework.web.servlet.ModelAndView;

public interface EmployeeService {

    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    void save(EmployeeDTO employeeDTO);

    PageResult getEmpList(EmployeePageQueryDTO employeePageQueryDTO);

    boolean setEmpStatus(Integer status, Long id);

    Employee getEmpById(Long id);

    boolean updateEmpInf(EmployeeDTO employeeDTO);
}
