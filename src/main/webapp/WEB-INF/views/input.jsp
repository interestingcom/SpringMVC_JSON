<%--导入JSTL标签库--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%--导入SpringMVC表单标签库--%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<%--
  Created by IntelliJ IDEA.
  User: liuqi
  Date: 2020/2/23
  Time: 15:31
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<%--SpringMVC的表单标签
            1.可以快速的开发表单
            2.可以更加方便的回显数据
    SpringMVC表单标签遇到的问题：
             Neither BindingResult nor plain target object
             for bean name 'command' available as request attribute

    问题原因：SpringMVC的表单标签必须要进行数据的回显，默认会使用"command"这个key
            到request中找回显的数据。如果找不到，则抛出异常。
    问题解决：让SpringMVC可以通过"command"从request中找到要回显的数据。
            还可以通过modelAttribute指定一个key替换默认的command

    表单标签在最终执行时会转化成原始的HTML标签。
--%>

<%--{pageContext.request.contextPath}==/SpringMVC_CRUD--%>
<form:form action="${pageContext.request.contextPath}/emp" method="post" modelAttribute="employee">
    <%--
        判断是添加操作还是修改操作：
            根据回显的Employee对象的id值来判断：如果有id就是修改，如果没有id就是添加操作
    --%>
    <c:if test="${!empty employee.id}" var="flag">
        <%--修改操作--%>
        <form:hidden path="id"/>
        <%--隐藏PUT--%>
        <input type="hidden" name="_method" value="PUT">
    </c:if>

    lastName:<form:input path="lastName"/><!--path就相当于HTML中input标签中的name属性-->
    <%--<input type="text" name="lastName">--%>
    <br>
    Email:<form:input path="email"/>
    <br>
    <%--radiobuttons 可以根据Map数据来生成单选框--%>
    Gender:<form:radiobuttons path="gender" items="${genders}"/>
    <br>
    deptName:<form:select path="department.id" items="${departments}" itemLabel="departmentName"
                          itemValue="id"></form:select>
    <br>
    <%-- <select name="department.id">
         <option value="1">开发部</option>
         <option value="2">测试版</option>
     </select>--%>
    <c:if test="${flag}">
        <input type="submit" value="Edit">
    </c:if>

    <c:if test="${!flag}">
        <input type="submit" value="ADD">
    </c:if>


</form:form>


</body>
</html>