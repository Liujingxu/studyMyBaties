package com.myBatis.dao;

import com.myBatis.entity.Employee;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

public interface EmployeeMapper {

    public Employee getEmpById(Integer id);

    public void insertEmp(Employee employee);

    public boolean deleteEmp(Integer id);

    public Integer updateEmp(Employee employee);

    public Employee getEmpByNameAndEmail(@Param("id") String last_name, @Param("email") String email);

    public Employee getEmpByMap(Map<String, Object> map);
}
