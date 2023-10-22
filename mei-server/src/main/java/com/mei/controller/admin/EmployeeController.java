package com.mei.controller.admin;

import com.mei.constant.JwtClaimsConstant;
import com.mei.dto.EmployeeDTO;
import com.mei.dto.EmployeeLoginDTO;
import com.mei.entity.Employee;
import com.mei.properties.JwtProperties;
import com.mei.result.Result;
import com.mei.service.EmployeeService;
import com.mei.utils.JwtUtil;
import com.mei.vo.EmployeeLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 员工管理
 */
@RestController
@RequestMapping("/admin/employee")
@Api(tags = "员工相关接口")
@Slf4j
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 登录
     *
     * @param employeeLoginDTO
     * @return
     */
    @PostMapping("/login")
    @ApiOperation(value = "用户登录接口")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录：{}", employeeLoginDTO);

        Employee employee = employeeService.login(employeeLoginDTO);

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();

        return Result.success(employeeLoginVO);
    }

    /**
     * 退出
     *
     * @return
     */
    @PostMapping("/logout")
    @ApiOperation(value = "用户退出的接口")
    public Result<String> logout() {
        return Result.success();
    }

    /**
     * @Title: 添加员工
     * @Author: Mr.Ka
     */
    @PostMapping()
    @ApiOperation("添加员工的接口")
    public Result<Object> addEmployee(@RequestBody EmployeeDTO employeeDTO) {
        log.info("req: {}", employeeDTO);
        employeeService.save(employeeDTO);
        return Result.success();
    }

}
