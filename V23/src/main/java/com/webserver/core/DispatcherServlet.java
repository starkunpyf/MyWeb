package com.webserver.core;

import com.webserver.annotation.Controller;
import com.webserver.annotation.RequestMapping;
import com.webserver.http.HttpServletPesponse;
import com.webserver.http.HttpServletRequest;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URISyntaxException;

public class DispatcherServlet {
    private static File staticDir;

    static{
        
        try {
            staticDir = new File(
                    ClientHandler.class.getClassLoader().getResource(
                            "./static"
                    ).toURI()
            );
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
    public void service(HttpServletRequest request, HttpServletPesponse response) throws URISyntaxException {
        String path = request.getRequestURI();
        System.out.println("请求路径:" + path);
        //判断该请求是否为请求某个业务
        /*"
        7'
            扫描com.webserver.controller包下的所有被@Controller标注的类
            并判断那些被@RequestMapping标注的方法中，那个该注解的参数值与当前
            path值相同，那么该方法就是处理该请求的方法，利用反射机制调用它即可
         */
        try {
            File dir = new File(
                    DispatcherServlet.class.getClassLoader().getResource(
                            "./com/webserver/controller"
                    ).toURI()
            );
            File[] subs = dir.listFiles(f -> f.getName().endsWith(".class"));
            for (File sub : subs) {
                String fileName = sub.getName();
                String className = fileName.substring(0, fileName.indexOf("."));
                Class cls = Class.forName("com.webserver.controller." + className);
                //判断该类是否被@Controller标注了
                if (cls.isAnnotationPresent(Controller.class)) {
                    Method[] methods = cls.getDeclaredMethods();
                    for (Method method : methods) {
                        //判断该方法是否被@RequestMapping标注了
                        if (method.isAnnotationPresent(RequestMapping.class)) {
                            //获取该注解
                            RequestMapping rm = method.getAnnotation(RequestMapping.class);
                            //获取该注解的参数(该方法处理的请求路径)
                            String value = rm.value();
                            if (path.equals(value)) {//判断当前请求是否为该方法处理的请求
                                //实例化该Controller
                                Object o = cls.newInstance();
                                //执行该方法
                                method.invoke(o, request, response);
                                return;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //去static目录下根据用户请求的抽象路径定位下面的文件
        File file = new File(staticDir, path);
        if (file.isFile()) {//实际存在的文件
            response.setContentFile(file);
        } else {//1:文件不存在  2:是一个目录
            response.setStatusCode(404);
            response.setStatusReason("NotFound");
            file = new File(staticDir, "/root/404.html");
            response.setContentFile(file);
        }
    }

}
