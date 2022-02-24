package com.webserver.controller;

import com.webserver.core.ClientHandler;
import com.webserver.entity.User;
import com.webserver.http.HttpServletRequst;
import com.webserver.http.HttpServletPesponse;

import java.io.*;
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
    /**
     * 处理用户登录
     * @param request
     * @param response
     */
    public void login(HttpServletRequst request, HttpServletPesponse response){
        System.out.println("开始处理登录...");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        System.out.println(username+","+password);

        if(username==null||password==null){
            File file = new File(staticDir,"/myweb/login_info_error.html");
            response.setContentFile(file);
            return;
        }

        File userFile = new File(USER_DIR,username+".obj");
        if(userFile.exists()){//该文件存在才说明是注册用户
            //反序列化该注册用户信息
            try(
                    FileInputStream fis = new FileInputStream(userFile);
                    ObjectInputStream ois = new ObjectInputStream(fis);
            ){
                User user = (User)ois.readObject();
                if(user.getPassword().equals(password)){
                    //登录成功
                    File file = new File(staticDir,"/myweb/login_success.html");
                    response.setContentFile(file);
                    return;
                }
            }catch(IOException | ClassNotFoundException e){
                e.printStackTrace();
            }
        }

        //如果程序能走到这里，就说明要么是用户名不对，要么是密码不对
        File file = new File(staticDir,"/myweb/login_fail.html");
        response.setContentFile(file);
        System.out.println("处理登录完毕!");
    }
}






