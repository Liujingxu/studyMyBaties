# studyMyBaties

### 描述
  　　这个项目是学习**myBaties**的学习线路，从*helloworld*到最后的ssm整合小demo,几乎一节课一个小版本
    
    
### 版本
  
  #### version1
  
  　　**myBaties** 的 *helloWorld* 小版本，初次使用**myBaties**写了一个查找一项内容的小栗子
  
  测试方法
  
  ```java
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
     
  ```java
          public SqlSessionFactory getSqlSessionFactoryl() throws IOException {
              String resource = "mybatis-config.xml";
              InputStream inputStream = Resources.getResourceAsStream(resource);
              return new SqlSessionFactoryBuilder().build(inputStream);
          }
          
  ```
    
  测试方法
    
  ```java
  public class Test{
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
  }
  ```
  
  #### version3.0
  
  　　测试`myBaties-config.xml`配置文件中的常用属性参数：
  
  + ##### version3.0
  
  　　通过`db.properties`文件将数据库连接操作分离出来。在`myBaties-config.xml`文件中进行修改如下:
  ```xml
    
    <properties resource="dbconfig.properties"/>
    
    <property name="driver" value="${jdbc.driver}"/>
    <property name="url" value="${jdbc.url}"/>
    <property name="username" value="${jdbc.username}"/>
    <property name="password" value="${jdbc.password}"/>
    
  ```
  
  
  + ##### version3.02
  
  　　测试`<typeAliases>`标签*起别名*属性,只需在`myBaties-config.xml`中添加标签，如下：
    
  ```xml
        <typeAliases>
          <typeAlias type="com.myBatis.entity.Employee" alias="emp" />
        </typeAliases>
  ```
  
  + ##### version3.03
      
      　　使用注解式的方式进行数据库操作,需要在*classpath*下创建一个新的接口,将所要操作的方法用正确的**Annotation**注解,与`*.xml`中的关键字相同.
    如下:
    
    ```java
      public interface EmployeeAnnotation {

        @Select("SELECT * FROM table_employee where id = #{id}")
         public Employee getEmpById(Integer id);
        }
    ```
    　　在`myBaties-config.xml`中的`<mappers>`中添加相应的`<mapper>`,如下:
    
    ```xml
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
      
   ```xml
      <!-- myBaties-config.xml -->
      <update id="updateEmp" >
        update table_employee set last_name = #{lastName}, gender = #{gender}, email = #{email} where id = #{id}
      </update>
   ```
   ```java
        // EmployeeMapper
        public Integer updateEmp(Employee employee);
   ```
   
   　　在进行增删改操作时，由于`sqlSession`在构造时有自动更新与手动更新两种,因此如果构造时没有参数,则默认为手动更新,需要调用`sqlSession.commit()`
     方法进行手动提交.以更新测试为例:
     
  ```java
        public class Test {
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
  ```
  
  + ##### version3.11
  
  　　实现多参数查询操作,如果在crud操作中出现多个约束条件,如果直接写sql语句会报错
     ```
     org.apache.ibatis.binding.BindingException: Parameter 'id' not found. Available parameters are [0, 1, param1, param2]
     ```
     两种解决方法:
   - 将`EmployeeMapper` 中的目标方法的参数名改为 `param1, param2, param3`
     
  ```xml
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
  ```java
  public class Test{
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
  + ##### version3.14
  　　测试查询结果封装入Map中,在 `EmployeeMapper`接口中的目标方法上添加注解
  `@MapKey("id"")`使**myBaties**能够识别并自动封装入Map中,结果如下:
  
  ```
    {1713010614=Employee{id=1713010614, lastName='Sam', email='tom@qq.com', gender=1}, 1713010615=Employee{id=1713010615, lastName='Yammy', email='yammy@qq.com', gender=0}, 1713010613=Employee{id=1713010613, lastName='Tom', email='tom@qq.com', gender=1}}
  ```
  
  + ##### version3.151
      测试自定义查询mapper。在配置文件中配置自定义mapper
      ```xml
        <resultMap id="myEmp" type="com.myBatis.entity.Employee">
                <id column="id" property="id" />
                <result column="last_name" property="lastName" />
                <result column="gender" property="gender" />
                <result column="email" property="email" />
            </resultMap>
     ```
     
     在查询语句中添加属性不再是`resultType`而是 `resultMap`调用刚刚写好得到自定义mapper类
     
     同时自定义mapper支持级联属性
       ```xml
        <resultMap id="myCplEmp" type="com.myBatis.entity.Employee">
                <id column="id" property="id" />
                <result column="last_name" property="lastName" />
                <result column="gender" property="gender" />
                <result column="email" property="email" />
                <result column="did" property="department.id" />
                <result column="dept_name" property="department.deptName" />
            </resultMap>
        
            <select id="getCplEmpById" resultMap="myCplEmp">
                select e.id id, last_name, gender, email, d.id did, dept_name
                 from table_employee e, table_dept d
                  where e.d_id = d.id
                      and e.id = #{id};
            </select>
       ```
       
       通过使用 *一级属性名.二级属性名*的方法进行级联查询。
       
  + ##### version3.152
  
    测试使用 `association`配置封装二级查询对象
    * 通过在自定义 `resultMap`中写入进行简单的封装，使用方法几乎和 `resultMap`中的用法一致。
    ```xml
    <association property="department" javaType="com.myBatis.entity.Department">
                <id column="id" property="id" />
                <result column="dept_name" property="deptName" />
            </association>
    
    ```
    
    * 通过该标签进行分布查询，通过简单的两个sql语句实现分级查询的效果
    
       具体通过写一个新接口 `DepartmentMapper` 来实现对department 数据进行操作的Dao层操作，其中存在一条通过Id查找department的方法
    ```xml
    <select id="getDeptById" resultType="com.myBatis.entity.Department" >
            select id, dept_name  from table_dept where id = #{id}
        </select>
    ```
      之后在 `EmployeeMyselfMapper` 中进行分级查询时调用接口的方法
      ```xml
        <resultMap id="myCplEmp3" type="com.myBatis.entity.Employee">
        <id column="id" property="id" />
        <result column="last_name" property="lastName" />
        <result column="gender" property="gender" />
        <result column="email" property="email" />
        <association property="department" select="com.myBatis.dao.DepartmentMapper.getDeptById" column="d_id">
        </association>
    </resultMap>
      ```
      这样设置仅需两条简单的sql语句就可以实现复杂查询效果。
      ```xml
      <!-- 原sql -->
        <select id="getCplEmpById2" resultMap="myCplEmp2">
                select e.id id, last_name, gender, email, d.id did, dept_name
                 from table_employee e, table_dept d
                  where e.d_id = d.id
                      and e.id = #{id};
            </select>
        <!-- 现sql -->
        <select id="getDeptById" resultType="com.myBatis.entity.Department" >
                select id, dept_name  from table_dept where id = #{id}
            </select>
        
        <select id="getCplEmpById3" resultMap="myCplEmp3">
                select * from table_employee where id = #{id}
            </select>
    
      ```
      
  + ##### version3.153
  
    测试用collection 来实现自定义*mapper*中的容器查询的功能，通过在 `resultMapper`中加入 `collection`标签。
    
    * 通过外连接的sql语句实现   
      ```xml
       <resultMap id="myDept" type="com.myBatis.entity.Department">
              <id column="did" property="id" />
              <result column="deptName" property="deptName" />
              <collection property="employees" ofType="com.myBatis.entity.Employee">
                  <id column="uid" property="id" />
                  <result column="lastName" property="lastName" />
                  <result column="email" property="email" />
                  <result column="gender" property="gender" />
              </collection>
          </resultMap>
      
          <select id="getDeptByIdplus" resultMap="myDept">
              select table_dept.id did, table_dept.dept_name deptName, table_employee.id uid, table_employee.last_name lastName, table_employee.email, table_employee.gender
               from table_dept left join table_employee on table_employee.d_id = table_dept.id
                where table_dept.id = #{id}
          </select>

      ```
      
      通过在 `myDept`内部的*collection*中进行定义容器的查询格式，与*resultMapper*中的定义方法基本相同。
      
     * 使用分布查询，使sql简单化
       ```xml
           <resultMap id="myDept2" type="com.myBatis.entity.Department">
                   <id column="id" property="id" />
                   <result column="dept_name" property="deptName" />
                   <collection property="employees" select="com.myBatis.dao.EmployeeMyselfMapper.getEmpByDeptId" column="id">
                   </collection>
               </resultMap>
           
               <select id="getDeptByIdplus2" resultMap="myDept2">
                   select id , dept_name from table_dept where id = #{id}
               </select>
            <select id="getEmpByDeptId" resultType="com.myBatis.entity.Employee">
                    select *  from table_employee where d_id = #{id}
                </select>
 
        ```
         在分布查询中， `getDeptByIdplus2`正常调用 `myDept2` 这个**resultMapper**并查询，然后在 `myDept2`的内部的 *collection* 中用到
         `getEmpByDeptId` 这个查询方法，参数 `column` 表示使用id列来查找，就像 `id = #{id}`
         
  #### version3.2
  　　　学习动态sql的相关操作
  + version3.20
    > 四种常见查询语法
    > 1. if
    > 2. choose(when, otherwise)
    > 3. trim(where, set)
    > 4. foreach
    
    使用语法 [OGNL语法](http://commons.apache.org/proper/commons-ognl/language-guide.html)
    
  + version3.21
  
    测试 *if* 语句，通过在 `<select></select>` 标签中插入 `<if test=""></if>` 来进行条件筛选，一般用于多条件查询
    
    ```xml
    <select id="getEmpsByConditionIf" resultType="com.myBatis.entity.Employee">
            select * from table_employee
            <where>
                <if test="id != null">
                    id = #{id}
                </if>
                <if test="lastName != null &amp;&amp; lastName != &quot;&quot;">
                    and last_name like #{lastName}
                </if>
                <if test="email != null &amp;&amp; email.trim() != &quot;&quot;">
                    and email = #{email}
                </if>
                <if test="gender == 0 or gender == 1">
                    and gender = #{gender}
                </if>
            </where>
        </select>

    ```
    
    将 *if* 语句插入 `<where></where>`标签中以解决 and 增多或缺少问题
  
  + version3.22
  
    测试 *trim* 语法， *choose* 语法， *set*语法
    
    + *trim* 一般与上文中 *if* 一起使用，用于增加与消除前缀后缀，它有四个属性。
       + **prefix**: 用于在语句前添加固定字符串;
       + **suffix**: 用于在语句后添加固定字符串
       + **prefixOverrides**: 用于删除不必要的固定字符串前缀;
       + **suffixOverrides**: 用于删除不必要的固定字符串后缀;
       
    + *choose* 一般用于单一条件查询，有些类似于 *switch*，内部有子标签 `<when></when>`和 `<otherwise></otherwise>`分别类似于 `case` 与 `default`   
    
    + *set* 用于编写更新语句，同样与上文 *if* 连用，通过该标签来添加 `set` 与消除多余的连接词。同时可以以 *trim* 的形式达到同样的效果。 *但是为什么我的没效果！！*
    