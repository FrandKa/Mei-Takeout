package com.mei.service.impl;

import com.mei.constant.MessageConstant;
import com.mei.constant.PasswordConstant;
import com.mei.constant.StatusConstant;
import com.mei.context.BaseContext;
import com.mei.dto.EmployeeDTO;
import com.mei.dto.EmployeeLoginDTO;
import com.mei.entity.Employee;
import com.mei.exception.AccountLockedException;
import com.mei.exception.AccountNotFoundException;
import com.mei.exception.PasswordErrorException;
import com.mei.mapper.EmployeeMapper;
import com.mei.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.Md5Crypt;
import org.apache.tomcat.util.security.MD5Encoder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletContext;
import java.time.LocalDateTime;

@Service
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * 员工登录
     *
     * @param employeeLoginDTO
     * @return
     */
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();

        //1、根据用户名查询数据库中的数据
        Employee employee = employeeMapper.getByUsername(username);

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //密码比对
        // TODO 后期需要进行md5加密，然后再进行比对
        password = DigestUtils.md5Hex(password);
        if (!password.equals(employee.getPassword())) {
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (employee.getStatus() == StatusConstant.DISABLE) {
            //账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        //3、返回实体对象
        return employee;
    }

    /**
     * 新增员工
     */
    @Override
    public void save(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        // 使用对象的属性拷贝机制: 从前面拷贝到后面(前提是属性名需要一致)
        BeanUtils.copyProperties(employeeDTO, employee);
        employee.setStatus(StatusConstant.ENABLE);
        // 设置密码
        String password = DigestUtils.md5Hex(PasswordConstant.DEFAULT_PASSWORD);
        employee.setPassword(password);
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());
        Long id = BaseContext.getCurrentId();
        employee.setCreateUser(id);
        employee.setUpdateUser(id);

        employeeMapper.insert(employee);
    }

}
