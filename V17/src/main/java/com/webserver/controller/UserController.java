package com.webserver.controller;

import com.webserver.core.ClientHandler;
import com.webserver.entity.User;
import com.webserver.http.HttpServletRequst;
import com.webserver.http.HttpServletPesponse;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URISyntaxException;

/**
 * MVC模型
 * M:model
 * V:view
 * C:controller
 *
 * 处理与用户相关的业务操作
 */
public class UserController {
    private static File USER_DIR = new File("./users");
    private static File staticDir;
    static{
        if(!USER_DIR.exists()){
            USER_DIR.mkdirs();
        }
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

    /**
     * 处理用户注册
     * @param request
     * @param response
     */
    public void reg(HttpServletRequst request,HttpServletPesponse response){
        System.out.println("开始处理用户注册...");
        //1获取用户表单提交上来的数据
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String nickname = request.getParameter("nickname");
        String ageStr = request.getParameter("age");
        System.out.println(username+","+password+","+nickname+","+ageStr);

        if(username==null||password==null||nickname==null||ageStr==null
        ||!ageStr.matches("[0-9]+")){
            File file = new File(staticDir,"myweb/reg_fail.html");
            response.setContentFile(file);
            return;
        }

        //2将用户信息以一个User实例形式表示，并序列化到文件中
        int age = Integer.parseInt(ageStr);
        User user = new User(username,password,nickname,age);
        //将该用户信息以用户名.obj的形式保存到users目录中
        File userFile = new File(USER_DIR,username+".obj");

        if(userFile.exists()){
            File file = new File(staticDir,"/myweb/have_user.html");
            response.setContentFile(file);
            return;
        }
        try(
                FileOutputStream fos = new FileOutputStream(userFile);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
        ){
            oos.writeObject(user);
            //注册成功了
            File file = new File(staticDir,"/myweb/reg_success.html");
            response.setContentFile(file);

        }catch(IOException e){

        }



        System.out.println("处理用户注册完毕!");
    }
}






