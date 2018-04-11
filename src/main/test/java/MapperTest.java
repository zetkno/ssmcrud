import com.atguigu.crud.bean.Department;
import com.atguigu.crud.bean.Employee;
import com.atguigu.crud.dao.DepartmentMapper;
import com.atguigu.crud.dao.EmployeeMapper;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.UUID;

/**
 * Created by Administrator on 2017/12/3 0003.
 * 测试dao层工作
 * 推荐Spring的项目就可以使用Spring的单元测试，可以自动注入我们需要的组件
 * @ContextConfiguration指定Spring配置文件的位置
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:applicationContext.xml"})
public class MapperTest {

    @Autowired
    DepartmentMapper departmentMapper;

    @Autowired
    EmployeeMapper employeeMapper;

    @Autowired
    SqlSession sqlSession;

//    测试DepartmentMapper
    @Test
    public void testCRUD() {
/*
//        1.创建SpringIco容器
        ApplicationContext ioc = new ClassPathXmlApplicationContext("applicationContext.xml");
//        从容器中获取mapper
        DepartmentMapper bean = ioc.getBean(DepartmentMapper.class);
        */
//        System.out.println(departmentMapper);
//        1.插入部门
//        departmentMapper.insertSelective(new Department(null, "开发部"));
//        departmentMapper.insertSelective(new Department(null, "测试部"));

//        2.测试插入员工
//        employeeMapper.insertSelective(new Employee(null, "Jerry", "M", "Jerry@163.com", 1 ));

//        3.批量插入多个员工，使用可执行批量操作的SQLSession
        EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
        for (int i = 0; i <1000 ; i++) {
            String uid = UUID.randomUUID().toString().substring(0, 5) + i;
            mapper.insertSelective(new Employee(null, uid, "M", uid+"@gmail.com", 1));

        }
        System.out.println("批量完成");



    }
}
