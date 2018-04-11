package com.atguigu.crud.service;

import com.atguigu.crud.bean.Employee;
import com.atguigu.crud.bean.EmployeeExample;
import com.atguigu.crud.dao.EmployeeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2017/12/3 0003.
 */
@Service
public class EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

//    查询所有员工
    public List<Employee> getAll() {

        return employeeMapper.selectByExampleWithDept(null);
    }

//    员工保存
    public void seveEmp(Employee employee) {
        employeeMapper.insertSelective(employee);
    }

    /**
     *
     * @param empName
     * @return true:代表当前姓名可用， false：不可用
     */
//检验用户名是否可用
    public boolean checkUser(String empName) {
        EmployeeExample example = new EmployeeExample();
        EmployeeExample.Criteria criteria = example.createCriteria();
        criteria.andEmpNameEqualTo(empName);
        long count = employeeMapper.countByExample(example); //有这条记录就返回大于0，没有就返回0
        return count == 0;
    }

    /**
     * 按照员工id查询
     * @param id
     * @return
     */
    public Employee getEmp(Integer id) {
        Employee employee = employeeMapper.selectByPrimaryKey(id);
        return employee;
    }

    /**
     * 员工更新
     * @param employee
     */
    public void updateEmp(Employee employee) {
        employeeMapper.updateByPrimaryKeySelective(employee);
    }

    /**
     * 员工删除
     * @param id
     */
    public void deleteEmp(Integer id) {
        employeeMapper.deleteByPrimaryKey(id);
    }

    public void deleteBatch(List<Integer> ids) {
        EmployeeExample example = new EmployeeExample();
        EmployeeExample.Criteria criteria = example.createCriteria();
//        delete from xxx where emp_id in(1,2,3);
        criteria.andEmpIdIn(ids);
        employeeMapper.deleteByExample(example);
    }
}
