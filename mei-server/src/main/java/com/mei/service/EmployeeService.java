package com.mei.service;

import com.mei.dto.EmployeeLoginDTO;
import com.mei.entity.Employee;

public interface EmployeeService {

    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

}
