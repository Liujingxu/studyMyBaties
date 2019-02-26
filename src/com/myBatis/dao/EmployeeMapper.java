package com.myBatis.dao;

import com.myBatis.entity.Employee;

public interface EmployeeMapper {

    public Employee getEmpById(Integer id);
}
