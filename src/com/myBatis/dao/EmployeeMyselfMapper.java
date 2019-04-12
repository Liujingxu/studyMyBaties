package com.myBatis.dao;

import com.myBatis.entity.Employee;

import java.util.List;

public interface EmployeeMyselfMapper {

    public Employee getEmpById(Integer id);

    public Employee getCplEmpById(Integer id);

    public Employee getCplEmpById2(Integer id);

    public Employee getCplEmpById3(Integer id);

    public List<Employee> getEmpByDeptId(Integer id);

    public Employee getEmpByChecking(Integer id);

    public List<Employee> getEmpsByConditionIf(Employee employee);

    public List<Employee> getEmpsByConditionTrim(Employee employee);

    public List<Employee> getEmpsByConditionChoose(Employee employee);

    public void updateEmpById(Employee employee);

    public void updateEmpById2(Employee employee);
}
