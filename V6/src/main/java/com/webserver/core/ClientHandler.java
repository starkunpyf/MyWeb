package com.webserver.core;

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

            //处理请求
            String path = requst.getUri();

            //3发送响应
            //临时测试:将resource目录中static/myweb/index.html响应给浏览器
            /*
                实际开发中，我们常用的相对路径都是类的加载路径。对应的写法:
                类名.class.getClassLoader().getResource("./")
                这里的"./"当前目录指的就是类加载路径的开始目录。它的实际位置
                JVM理解的就是当前类的包名指定中最上级包的上一层。
                例如下面的代码中，当前类ClientHandler指定的包:
                package com.webserver.core;
                那么包的最上级就是com，因此类加载路径的开始目录就是com的上级目录
                实际就是项目的target/classes这个目录了。
                maven项目编译后会将src/main/java目录和src/main/resource目录
                最终合并到target/classes中。
             */
            File file = new File(
                    ClientHandler.class.getClassLoader().getResource(
                            "./static" + path
                    ).toURI()
            );
            /*
                HTTP/1.1 200 OK(CRLF)
                Content-Type: text/html(CRLF)
                Content-Length: 2546(CRLF)(CRLF)
                1011101010101010101......
             */

            //3.1发送状态行
            String line = "HTTP/1.1 200 OK";
            println(line);
            //3.2发送响应头
            line = "Content-Type: text/html";
            println(line);

            line = "Content-Length: "+file.length();
            println(line);
            //单独发送回车+换行表示响应头部分发送完毕
            println("");

            //3.3发送响应正文
            OutputStream out = socket.getOutputStream();
            byte[] buf = new byte[1024*10];
            int len;
            FileInputStream fis = new FileInputStream(file);
            while((len = fis.read(buf))!=-1){
                out.write(buf,0,len);
            }
            System.out.println("响应发送完毕");

        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }finally{
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void println(String line) throws IOException {
        OutputStream out = socket.getOutputStream();
        byte[] data = line.getBytes(StandardCharsets.ISO_8859_1);
        out.write(data);
        out.write(13);//发送回车符
        out.write(10);//发送换行符

    }
}
