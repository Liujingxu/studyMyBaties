package com.myBatis.dao;

import com.myBatis.entity.Employee;

public interface EmployeeMapper {

    public Employee getEmpById(Integer id);

    public void insertEmp(Employee employee);

    public boolean deleteEmp(Integer id);

    public Integer updateEmp(Employee employee);
}
