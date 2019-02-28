package com.myBatis.entity;

import com.myBatis.dao.EmployeeAnnotation;
import com.myBatis.dao.EmployeeMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;

class EmployeeTest {


    public SqlSessionFactory getSqlSessionFactoryl() throws IOException {
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        return new SqlSessionFactoryBuilder().build(inputStream);
    }

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

    @Test
    public void test2() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactoryl();
        SqlSession sqlSession = sqlSessionFactory.openSession();

        try {
            EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
            Employee employee = mapper.getEmpById(1713010618);
            System.out.println(employee);
        }finally {
            sqlSession.close();
        }
    }

    @Test
    public void test3() throws IOException {
        SqlSessionFactory sqlSessionFactory  = getSqlSessionFactoryl();
        SqlSession sqlSession = sqlSessionFactory.openSession();

        try {
            EmployeeAnnotation mapper = sqlSession.getMapper(EmployeeAnnotation.class);
            Employee emp = mapper.getEmpById(1713010613);

            System.out.println(emp);
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void test4() throws IOException {

        SqlSessionFactory sqlSessionFactory = getSqlSessionFactoryl();
        SqlSession sqlSession = sqlSessionFactory.openSession();
        EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);

        try {
            mapper.insertEmp(new Employee(1713010618, "Ezio", "ezio@qq.com", "1"));
            sqlSession.commit();
        }finally {
            sqlSession.close();
        }
    }

    @Test
    public void test5() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactoryl();
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);

            System.out.println(mapper.deleteEmp(1713010618));
            sqlSession.commit();
        } finally {
            sqlSession.close();
        }
    }


    @Test
    public void test6() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactoryl();
        SqlSession sqlSession = sqlSessionFactory.openSession();
        EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);

        try {
            Integer emp = mapper.updateEmp(new Employee(1713010615, "Yammy", "yammy@qq.com", "0"));
            sqlSession.commit();
            System.out.println("there are " + emp + " changes");
        } finally {
            sqlSession.close();
        }

    }


}