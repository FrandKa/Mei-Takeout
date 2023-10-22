package com.mei.controller.admin;

import com.mei.constant.JwtClaimsConstant;
import com.mei.constant.MessageConstant;
import com.mei.dto.EmployeeDTO;
import com.mei.dto.EmployeeLoginDTO;
import com.mei.dto.EmployeePageQueryDTO;
import com.mei.entity.Employee;
import com.mei.enumeration.OperationType;
import com.mei.properties.JwtProperties;
import com.mei.result.PageResult;
import com.mei.result.Result;
import com.mei.service.EmployeeService;
import com.mei.utils.JwtUtil;
import com.mei.vo.EmployeeLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.ResultContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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

    // 分页查找员工的信息:
    @GetMapping("/page")
    @ApiOperation("分页查找员工信息")
    public Result<PageResult> getUserInfPage(EmployeePageQueryDTO employeePageQueryDTO) {
        log.info("员工分页查询: {}", employeePageQueryDTO);
        PageResult result = employeeService.getEmpList(employeePageQueryDTO);


        return Result.success(result);
    }

    /**
     * 启用禁用账号
     *
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    @ApiOperation("启用禁用员工账号接口")
    public Result setEmployeeStatus(
            @PathVariable("status") Integer status,
            @RequestParam("id") Long id
    ) {
        log.info("启用禁用员工账号: {}, {}", status, id);
        boolean flag = employeeService.setEmpStatus(status, id);
        if (flag) {
            return Result.success();
        }
        return Result.error(MessageConstant.ACCOUNT_NOT_FOUND);
    }

    @PutMapping()
    @ApiOperation("修改员工信息")
    public Result updateEmpInf(@RequestBody EmployeeDTO employeeDTO) {
        boolean flag = employeeService.updateEmpInf(employeeDTO);
        if(flag) {
            return Result.success();
        }
        return Result.error(MessageConstant.ACCOUNT_NOT_FOUND);
    }

    @GetMapping("/{id}")
    @ApiOperation("根据id查询员工信息")
    public Result<Employee> getEmpInfById(@PathVariable("id") Long id) {
        Employee data = employeeService.getEmpById(id);
        if(Objects.nonNull(data)) {
            return Result.success(data);
        }
        return Result.error(MessageConstant.ACCOUNT_NOT_FOUND);
    }

}
