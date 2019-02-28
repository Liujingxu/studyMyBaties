package com.myBatis.dao;

import com.myBatis.entity.Employee;
import org.apache.ibatis.annotations.Select;

public interface EmployeeAnnotation {

    @Select("SELECT * FROM table_employee where id = #{id}")
    public Employee getEmpById(Integer id);
}
