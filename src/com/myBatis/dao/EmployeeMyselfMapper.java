package com.myBatis.dao;

import com.myBatis.entity.Employee;

import java.util.List;

public interface EmployeeMyselfMapper {

    public Employee getEmpById(Integer id);

    public Employee getCplEmpById(Integer id);

    public Employee getCplEmpById2(Integer id);

    public Employee getCplEmpById3(Integer id);

    public List<Employee> getEmpByDeptId(Integer id);
}
