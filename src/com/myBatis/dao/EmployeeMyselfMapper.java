package com.myBatis.dao;

import com.myBatis.entity.Employee;

public interface EmployeeMyselfMapper {

    public Employee getEmpById(Integer id);

    public Employee getCplEmpById(Integer id);
}
