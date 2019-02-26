package com.myBatis.entity;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;

class EmployeeTest {

    @Test
    public void testEmployee() throws IOException {

        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

        SqlSession sqlSession = sqlSessionFactory.openSession();

        try {
            Employee employee = sqlSession.selectOne("com.myBatis.EmployeeMapper.selectEmp", 1713010613);

            System.out.println(employee);
        } finally {
            sqlSession.close();
        }

    }


}