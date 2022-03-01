package com.webserver.core;

import com.webserver.annotation.Controller;
import com.webserver.annotation.RequestMapping;
import com.webserver.http.HttpServletPesponse;
import com.webserver.http.HttpServletRequest;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
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
        HandlerMapping.MethodMapping methodMapping= HandlerMapping.getMethod(path);
        if(methodMapping!=null){
            Object controller = methodMapping.getController();
            Method method = methodMapping.getMethod();
            try {
                method.invoke(controller, request, response);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
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
