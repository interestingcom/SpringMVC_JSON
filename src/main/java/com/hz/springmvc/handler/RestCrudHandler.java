package com.hz.springmvc.handler;

import com.hz.springmvc.beans.Department;
import com.hz.springmvc.beans.Employee;
import com.hz.springmvc.dao.DepartmentDao;
import com.hz.springmvc.dao.EmployeeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Controller
public class RestCrudHandler {

    @Autowired
    private EmployeeDao employeeDao;

    @Autowired
    private DepartmentDao departmentDao;

    //测试拦截器
    @RequestMapping("testInterceptor")
    public  String testInterceptor(){

        return  "success";
    }

    //文件上传
    @RequestMapping("/upload")
    public String testUploadFile(@RequestParam("desc")String desc,
                                 @RequestParam("uploadFile")MultipartFile uploadFile,
                                HttpSession session) throws Exception {
        //获取到上传文件的名字
        String uploadFileName=uploadFile.getOriginalFilename();
        //获取输入流
 //       InputStream in=uploadFile.getInputStream();
        //获取服务器端的upload文件夹的真实路径
        ServletContext sc=session.getServletContext();

        String realPath=sc.getRealPath("uploads");

        File targetFile=new File(realPath+"/"+uploadFileName);

 //       FileOutputStream os=new FileOutputStream(targetFile);

//        //写文件
//        int i;
//        while ((i=in.read())!=-1){
//            os.write(i);
//        }
//
//        in.close();
//        os.close();
        uploadFile.transferTo(targetFile);
        return "success";
    }



    /*
    * 支持 @RequestBody @ResponseBody HttpEntity ResponseEntity
    *
    * 下载的原来：将服务器的文件 以流的形式 写到客户端。
    *
    * ResponseEntity:将要下载的文件数据，以及响应信息封装到ResponseEntity对象中，浏览器通过解析
    *               发送回去的响应数据，就可以进行下一个操作。
    *
    * */
    @RequestMapping("/download")
    public ResponseEntity<byte[]> testDownload(HttpSession session)throws Exception{
        //将要下载的文件读取成一个字节数据
        byte[] imgs;

        ServletContext sc=session.getServletContext();

        InputStream in=sc.getResourceAsStream("image/ll.PNG");
        System.out.println(in);
        System.out.println(in.available());
        imgs=new byte[in.available()];
        in.read(imgs);

        //将响应数据 以及一些响应头信息封装到ResponseEntity中
        /*
        * 参数：
        *   1.发送给u浏览器端的数据
        *   2.设置响应头
        *   3.设置响应码
        * */
        HttpHeaders httpHeaders=new HttpHeaders();
        httpHeaders.add("Content-Disposition","attachment;filename=rr.png");

        HttpStatus statusCode=HttpStatus.OK;//200

        ResponseEntity<byte[]> re=new ResponseEntity<byte[]>(imgs,httpHeaders,statusCode);

        return re;

    }

    //处理Json
    @ResponseBody//负责将方法的返回值 转化成json字符串 响应给浏览器端。
    @RequestMapping("/testJson")
    public Collection<Employee> testJson(){
        Collection<Employee> emps=employeeDao.getAll();

        //Gson gson.toJson(emps),out.println(jsonStr);

        return emps;
    }

    //修改功能：具体的修改操作
    @RequestMapping(value = "/emp",method = RequestMethod.PUT)
    public String updateEmp(Employee employee){
        employeeDao.save(employee);

        return "redirect:/emps";
    }

    //修改功能：去往修改页面
    @RequestMapping(value = "/emp/{id}",method = RequestMethod.GET)
    public String toUpdatePage(@PathVariable("id")Integer id,Map<String,Object> map){
        //查询要修改的员工信息
        Employee employee=employeeDao.get(id);
        map.put("employee",employee);

        //页面中显示部门下拉列表的数据
        Collection<Department> departments=departmentDao.getDepartments();
        map.put("departments",departments);

        //构造页面中生成单选框的数据
        Map<String,String> genders=new HashMap<>();
        genders.put("0","女");
        genders.put("1","男");
        map.put("genders",genders);

        //去往修改页面
        return "input";
    }


    //删除功能
    @RequestMapping(value = "/emp/{id}",method = RequestMethod.DELETE)
    public String deleteEmp(@PathVariable("id") Integer id){
        //删除员工
        employeeDao.delete(id);

        return "redirect:/emps";
    }


    //添加功能:具体的添加操作
    @RequestMapping(value = "/emp",method = RequestMethod.POST)
    public  String addEmp(Employee employee){
            //添加员工
        employeeDao.save(employee);

        //回到列表页面：重定向到显示所有员工信息列表的请求

        return "redirect:/emps";
    }


    /*
    * 添加功能：去往添加页面
    *
    * 添加页面需要部门数据
    *
    * */
    @RequestMapping(value = "/emp",method = RequestMethod.GET)
    public String toAddPage(Map<String,Object> map){
        Collection<Department> departments = departmentDao.getDepartments();

        map.put("departments",departments);

        //2.构造页面中生成单选框的数据
        Map<String,String> genders=new HashMap<>();

        genders.put("0","女");
        genders.put("1","男");

        map.put("genders",genders);

        //3.设置页面中要回显的数据
        //map.put("command",new Employee());
     //   Employee employee=new Employee();
     //   employee.setLastName("hello");
        map.put("employee",new Employee());

        return "input";
    }



    //显示所有的员工信息列表
    @RequestMapping(value = "/emps",method = RequestMethod.GET)
    public String listAllEmps(Map<String,Object> map){
        Collection<Employee> empst=employeeDao.getAll();
        map.put("emps",empst);

       return "list";

    }
}
