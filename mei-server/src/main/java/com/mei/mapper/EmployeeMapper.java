package com.mei.mapper;

import com.github.pagehelper.Page;
import com.mei.annotation.AutoFill;
import com.mei.dto.EmployeePageQueryDTO;
import com.mei.entity.Employee;
import com.mei.enumeration.OperationType;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface EmployeeMapper {

    /**
     * 根据用户名查询员工
     * @param username
     * @return
     */
    @Select("select * from mei_take_out.employee where username = #{username}")
    Employee getByUsername(String username);

    @AutoFill(OperationType.INSERT)
    @Insert("INSERT INTO mei_take_out.employee " +
            "(name, username, password, phone, sex, id_number, status, create_time, update_time, create_user, update_user)" +
            "VALUES " +
            "(#{name}, #{username}, #{password}, #{phone}, #{sex}, #{idNumber}, #{status}, #{createTime}, #{updateTime}, #{createUser}, #{updateUser}) ;")
    void insert(Employee employee);

    // 分页查询:
    Page<Employee> queryList(EmployeePageQueryDTO employeePageQueryDTO);

    @AutoFill(OperationType.UPDATE)
    int update(Employee employee);

    Employee queryById(Long id);
}
