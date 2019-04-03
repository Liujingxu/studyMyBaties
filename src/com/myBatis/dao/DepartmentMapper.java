package com.myBatis.dao;

import com.myBatis.entity.Department;

public interface DepartmentMapper {

    public Department getDeptById(Integer id);

    public Department getDeptByIdplus(Integer id);

    public Department getDeptByIdplus2(Integer id);
}
