package com.webserver.core;

import com.webserver.controller.UserController;
import com.webserver.http.HttpContext;
import com.webserver.http.HttpServletPesponse;
import com.webserver.http.HttpServletRequst;

import java.io.File;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
    public void service(HttpServletRequst request, HttpServletPesponse response){
        String path = request.getRequestURI();
        System.out.println("dis请求路径:"+path);
        System.out.println("分支前");
        if("/myweb/reg".equals(path)){
            UserController controller = new UserController();
            controller.reg(request,response);

        }else{
            File file = new File(staticDir,path);
            if(file.isFile()){//实际存在的文件
                response.setContentFile(file);


            }else{//1:文件不存在  2:是一个目录
                response.setStatusCode(404);
                response.setStatusReason("NotFound");
                file = new File(staticDir,"/root/404.html");
                response.setContentFile(file);

            }
        }



    }
}
