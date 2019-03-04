package com.myBatis.dao;

import com.myBatis.entity.Employee;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface EmployeeMapper {

    public Employee getEmpById(Integer id);

    public void insertEmp(Employee employee);

    public boolean deleteEmp(Integer id);

    public Integer updateEmp(Employee employee);

    public Employee getEmpByNameAndEmail(@Param("id") String last_name, @Param("email") String email);

    public Employee getEmpByMap(Map<String, Object> map);

    public List<Employee> getEmpLikeName(String lastName);

    @MapKey("id")
    public Map<Integer, Employee> getEmpLikeNameByMap(String lastName);


}
