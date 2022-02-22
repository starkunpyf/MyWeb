package com.webserver.core;

import com.webserver.http.HttpServletPesponse;
import com.webserver.http.HttpServletRequst;

import java.io.*;
import java.net.Socket;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ClientHandler implements Runnable{
    private Socket socket;

    public ClientHandler(Socket socket){
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            //解析请求
            HttpServletRequst requst = new HttpServletRequst(socket);
            HttpServletPesponse response = new HttpServletPesponse(socket);
            //处理请求
            DispatcherServlet servlet = new DispatcherServlet();
            servlet.service(requst,response);
            response.Response();


        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
