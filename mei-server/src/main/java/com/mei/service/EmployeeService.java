package com.mei.service;

import com.mei.dto.EmployeeDTO;
import com.mei.dto.EmployeeLoginDTO;
import com.mei.entity.Employee;
import org.springframework.web.servlet.ModelAndView;

public interface EmployeeService {

    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    void save(EmployeeDTO employeeDTO);
}
