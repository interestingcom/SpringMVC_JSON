package com.hz.springmvc.interceptor;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//自定义拦截器
public class MyFirstInterceptor implements HandlerInterceptor {

    //1.是在DispatcherServlet的939行 在请求处理方法之前执行
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("MyFirstInterceptor  preHandle");
        return true;
    }

    //2.在DispatcherServlet 959行 请求处理方法之后，视图处理之前执行。
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        System.out.println("MyFirstInterceptor  postHandle");
    }

    //3.在dispatcherServlet的1030行，视图处理之后执行。(转发/重定向后执行)
    /*
    *
    * [1].在DispatcherServlet的1030行 视图处理之后执行。（转发/重定向后执行）
    * [2].当某个拦截器的preHandler返回false后，也会执行当前拦截器之前拦截器的afterCompletion.
    * [3].当DispatcherServlet的doDispath方法抛出异常，也可能执行拦截器的afterCompletion.
    * */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        System.out.println("MyFirstInterceptor  afterCompletion");
    }
}
