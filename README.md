# studyMyBaties

### 描述
  　　这个项目是学习**myBaties**的学习线路，从*helloworld*到最后的ssm整合小demo,几乎一节课一个小版本
    
    
### 版本
  
  #### version1
  
  　　**myBaties** 的 *helloWorld* 小版本，初次使用**myBaties**写了一个查找一项内容的小栗子
  
  测试方法
  
  ```
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
  
  ```
  
  
  #### version2 
  
  　　添加接口*EmployeeMapper*以及*EmployeeMapper.xml*文件。并创建测试方法。感受**mybaties**的接口化编程，体会**myBaties**能够由开发者自己编辑sql
    语句，写了一个查找的小栗子。封装了一个方法用来得到`SqlSessionFactory`对象，解决一些重复操作如下：
     
  ```
          public SqlSessionFactory getSqlSessionFactoryl() throws IOException {
              String resource = "mybatis-config.xml";
              InputStream inputStream = Resources.getResourceAsStream(resource);
              return new SqlSessionFactoryBuilder().build(inputStream);
          }
          
  ```
    
  测试方法
    
  ```
      public void test2() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactoryl();
        SqlSession sqlSession = sqlSessionFactory.openSession();

        try {
            EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
            Employee employee = mapper.getEmpById(1713010614);
            System.out.println(employee);
        }finally {
            sqlSession.close();
        }
    }
  
  ```
  
  #### version3.0
  
  　　测试`myBaties-config.xml`配置文件中的常用属性参数：
  
  + ##### version3.0
  
  　　通过`db.properties`文件将数据库连接操作分离出来。在`myBaties-config.xml`文件中进行修改如下:
  ```
    
    <properties resource="dbconfig.properties"/>
    
    <property name="driver" value="${jdbc.driver}"/>
    <property name="url" value="${jdbc.url}"/>
    <property name="username" value="${jdbc.username}"/>
    <property name="password" value="${jdbc.password}"/>
    
  ```
  
  
  + ##### version3.02
  
  　　测试`<typeAliases>`标签*起别名*属性,只需在`myBaties-config.xml`中添加标签，如下：
    
  ```
        <typeAliases>
          <typeAlias type="com.myBatis.entity.Employee" alias="emp" />
        </typeAliases>
  ```
  
  + ##### version3.03
      
      　　使用注解式的方式进行数据库操作,需要在*classpath*下创建一个新的接口,将所要操作的方法用正确的**Annotation**注解,与`*.xml`中的关键字相同.
    如下:
    
    ```
      public interface EmployeeAnnotation {

        @Select("SELECT * FROM table_employee where id = #{id}")
         public Employee getEmpById(Integer id);
        }
    ```
    　　在`myBaties-config.xml`中的`<mappers>`中添加相应的`<mapper>`,如下:
    
    ```
        <mappers>
          <mapper resource="EmployeeMapper.xml" />
          <mapper class="com.myBatis.dao.EmployeeAnnotation" />
        </mappers>
    ```
    
    > **mapper配置的四种形式**
    > 1. 使用相对于类路径的资源引用: 
        ` <mapper resource="com/myBaties/conf/EmployeeMapper.xml" />`
    > 2. 使用完全限定资源定位符(URL),*可以是网络路径或者系统完整路径* :
        `
          <mapper url="XXX/XXX/XXX.xml" />
        `
    > 3. 使用映射器接口实现类的完全限定类名:
        `
          <mapper class="com.myBatis.dao.EmployeeAnnotation" />
        `
    > 4. 批量操作将包内所有映射器接口注册为映射器:
       `
         <package name="com.myBatis.dao" />
       `
  #### version3.1
  　　学习映射文件的相关操作
  + ##### version3.10
   　　　实现基本的增删改查操作.在`EmployeeMapper`中添加增删改的操作方法,在`myBaties-config.xml`中加入相应的sql配置,其中删除与查找操作传入参数为
      `Integer`型的`id`属性,增加与更新操作传入参数为 javaBean `Employee`类型.以更新操作为例,如下:
      
   ```
      <!-- myBaties-config.xml -->
      <update id="updateEmp" >
        update table_employee set last_name = #{lastName}, gender = #{gender}, email = #{email} where id = #{id}
      </update>
   ```
   ```
        // EmployeeMapper
        public Integer updateEmp(Employee employee);
   ```
   
   　　在进行增删改操作时，由于`sqlSession`在构造时有自动更新与手动更新两种,因此如果构造时没有参数,则默认为手动更新,需要调用`sqlSession.commit()`
     方法进行手动提交.以更新测试为例:
     
  ```
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
  ```
  
  + ##### version3.11
  
  　　实现多参数查询操作,如果在crud操作中出现多个约束条件,如果直接写sql语句会报错
     ```
     org.apache.ibatis.binding.BindingException: Parameter 'id' not found. Available parameters are [0, 1, param1, param2]
     ```
     两种解决方法:
   - 将`EmployeeMapper` 中的目标方法的参数名改为 `param1, param2, param3`
     
  ```
        <select id="getEmpByNameAndEmail" resultType="com.myBatis.entity.Employee">
          select * from table_employee where last_name = #{param1} and email = #{param2};
        </select>
  ```
   - 在`EmployeeMapper` 中的目标方法的参数前加入注解`@param("参数名")`
  
  ```
  public Employee getEmpByNameAndEmail(@Param("id") String last_name, @Param("email") String email);
  ```
  
 + ##### version3.12
     可以通过传入一个`Map`对象,通过**key**键直接传入参数.使用如下
  ```
      public void test8() throws IOException {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactoryl();
        SqlSession sqlSession = sqlSessionFactory.openSession();
        EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
        Map<String, Object> map = new HashMap<String, Object>();

        map.put("id", 1713010614);
        map.put("lastName", "Sam");

        try {
            Employee emp = mapper.getEmpByMap(map);

            System.out.println(emp);
        } finally {
            sqlSession.close();
        }

    }
  
  ```
 ##### 常用映射方式
  1. 最好使用数据模型(javaBean)进行传输
  2. 当传入参数不是数据模型时使用**map**操作
  3. 当传入参数是常用数据但不是数据模型使用TO模型
  
  + ##### version3.13
  　　测试模糊查询得到结果存入List中，注意在 `EmployeeMapper.xml`
  中配置时 `resultType` 属性不应是`List`类型，而应该是`Employee`,
  他会自动转为集合返回。如下：
  ```xml
      <select id="getEmpLikeName" resultType="com.myBatis.entity.Employee">
          select * from table_employee where last_name like #{lastName}
      </select>
 ```
  
 
  
