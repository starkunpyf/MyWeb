package com.webserver.core;

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
    public void service(HttpServletRequst requst, HttpServletPesponse response){
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

            /*
                HTTP/1.1 200 OK(CRLF)
                Content-Type: text/html(CRLF)
                Content-Length: 2546(CRLF)(CRLF)
                1011101010101010101......
             */

        //3.1发送状态行
        File file = new File(staticDir,path);

        if(file.isFile()){//实际存在的文件
            response.setContentFile(file);
            /*
                实现根据资源的后缀名设置正确的Content-Type的值(参阅http.txt文件最下面)
                思路:
                1:首先根据file获取其表示的文件名
                2:根据文件名截取出后缀名
                3:根据后缀设置Content-Type的值
             */
            Map<String,String> mimeMapping = new HashMap<>();
            mimeMapping.put("html","text/html");
            mimeMapping.put("css","text/css");
            mimeMapping.put("js","application/javascript");
            mimeMapping.put("gif","image/gif");
            mimeMapping.put("jpg","image/jpeg");
            mimeMapping.put("png","image/png");
            //获取文件的后缀名   image.png
            String fileName = file.getName();
            String ext = fileName.substring(fileName.lastIndexOf(".")+1);
            //根据后缀名提取对应的mime类型
            String mime = mimeMapping.get(ext);

            response.addHeader("Content-Type","text/html");
            response.addHeader("Content-Length",String.valueOf(file.length()));
        }else{//1:文件不存在  2:是一个目录
            response.setStatusCode(404);
            response.setStatusReason("NotFound");
            file = new File(staticDir,"/root/404.html");
            response.setContentFile(file);
            response.addHeader("Content-Type","text/html");
            response.addHeader("Content-Length",String.valueOf(file.length()));
        }
    }
}
