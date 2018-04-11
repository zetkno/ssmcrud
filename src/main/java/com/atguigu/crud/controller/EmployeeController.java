package com.atguigu.crud.controller;

import com.atguigu.crud.bean.Employee;
import com.atguigu.crud.bean.Msg;
import com.atguigu.crud.service.EmployeeService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 处理员工crud请求
 * Created by Administrator on 2017/12/3 0003.
 */
@Controller
public class EmployeeController{

    @Autowired
    private EmployeeService employeeService;

    @ResponseBody
    @RequestMapping(value = "/emp/{ids}",method = RequestMethod.DELETE)
    public Msg deleteEmpById(@PathVariable("ids")String ids){
//        批量删除
        if(ids.contains("-")){
            List<Integer> del_ids = new ArrayList<Integer>();
            String[] str_ids = ids.split("-");
            //组装id的集合
            for (String string:str_ids) {
                del_ids.add(Integer.parseInt(string));
            }
            employeeService.deleteBatch(del_ids);
//            单个删除
        } else {
            Integer id = Integer.parseInt(ids);
            employeeService.deleteEmp(id);
        }
        return Msg.success();
    }

    /**
     * 员工更新方法
     * @param employee
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/emp/{empId}", method = RequestMethod.PUT)
    public Msg saveEmp(Employee employee,HttpServletRequest request) {
//        System.out.println("请求体中的值："+request.getParameter("gender"));
//        System.out.println("将要更新的数据，" + employee);
        employeeService.updateEmp(employee);
        return Msg.success();
    }

    /**
     * 根据id查询员工
     * @param id
     * @return
     */
    @RequestMapping(value = "/emp/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Msg getEmp(@PathVariable("id")Integer id) {

        Employee employee = employeeService.getEmp(id);
        return Msg.success().add("emp",employee);
    }

    /**
     * 检查用户名是否可用
     * @param empName
     * @return
     */
    @ResponseBody
    @RequestMapping("/checkuser")
    public Msg checkuser(@RequestParam("empName")String empName) {
//        先判断用户名是否是合法的表达式
        String regx = "(^[a-zA-Z0-9_-]{3,16}$)|(^[\\u2E80-\\u9FFF]{2,5})";
        if(!empName.matches(regx)) {
            return Msg.fail().add("va_msg", "用户名可以是2-5位中文或者6-16位英文和数字的组合(后台)");
        }
        //数据库用户名重复校验
       boolean b = employeeService.checkUser(empName);
       if(b) {
           return Msg.success();
       } else {
           return Msg.fail().add("va_msg", "用户名不可用");
       }
    }

    /**
     * 员工保存
     * 支持JSR303校验
     * 导入hibernate-Validator
     * @param employee
     * @return
     */
    @RequestMapping(value = "/emp", method = RequestMethod.POST)
    @ResponseBody
    public Msg saveEmp(@Valid Employee employee, BindingResult result) {
        if(result.hasErrors()) {
            //校验失败，应该返回失败，在模态框中显示校验失败的错误信息
            Map<String, Object> map = new HashMap<String, Object>();
            List<FieldError> errors = result.getFieldErrors();
            for (FieldError fieldError : errors) {
                System.out.println("错误字段名：" + fieldError.getField());
                System.out.println("错误信息：" + fieldError.getDefaultMessage());
                map.put(fieldError.getField(), fieldError.getDefaultMessage());
            }
            return Msg.fail().add("errorFields", map);
        } else {
            employeeService.seveEmp(employee);
            return Msg.success();
        }

    }

    /**
     * 查询员工数据（分页查询）
     * @return
     */

    @RequestMapping("/emps")
    @ResponseBody//将PageInfo对象转换为JSON
    public Msg getEmpsWithJson(@RequestParam(value="pn", defaultValue = "1")Integer pn, Model model) {
        //        引入pageHelper分页插件
//        在查询之前只需调用，传入页码，以及每页的大小
        PageHelper.startPage(pn, 5);

        List<Employee> emps = employeeService.getAll();
//        封装了详细的分页信息，包括有我们查询出来的数据
        PageInfo page = new PageInfo(emps, 5);

        return Msg.success().add("pageInfo", page);
    }


//    @RequestMapping("/emps")
    public String getEmps(@RequestParam(value="pn", defaultValue = "1")Integer pn, Model model) {

//        引入pageHelper分页插件
//        在查询之前只需调用，传入页码，以及每页的大小
        PageHelper.startPage(pn, 5);

        List<Employee> emps = employeeService.getAll();
//        封装了详细的分页信息，包括有我们查询出来的数据
        PageInfo page = new PageInfo(emps, 5);
        model.addAttribute("pageInfo", page);
        return "list";
    }
}
