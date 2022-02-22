package com.webserver.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import static com.webserver.http.HttpContext.CR;
import static com.webserver.http.HttpContext.LF;

public class HttpServletRequst {
    private Socket socket;
    private String method;
    private String uri;
    private String protocol;
    private Map<String, String> headers = new HashMap();


    public HttpServletRequst(Socket socket) throws IOException {
        this.socket = socket;
        //解析请求行
        parseRequstLine();
        //解析消息头
        parseHeaders();
        //解析消息正文
        parseContent();

    }
    private void parseRequstLine() throws IOException {
        String string = readLine();
        System.out.println(string);
        String[] strings = string.split(" ");
        method = strings[0];
        uri = strings[1];
        protocol = strings[2];

        System.out.println("method: " + method );
        System.out.println("uri: " + uri );
        System.out.println("protocol: " + protocol );
    }

    private void parseHeaders() throws IOException {
        while (true){
            String string = readLine();
            if (string.isEmpty()){
                break;
            }
            String[] kvStrings = string.split(": ");
            String key = kvStrings[0];
            String value = kvStrings[1];
            headers.put(key, value);
            //System.out.println(string);
        }
        Set<Map.Entry<String, String>> entrySet = headers.entrySet();
//        for(Map.Entry<String, String> entry : entrySet){
//            System.out.println(entry.getKey() + ":" + entry.getValue());
//        }
//        System.out.println(headers);
    }

    private void parseContent(){

    }


    private String readLine() throws IOException {
        InputStream in = socket.getInputStream();
        int d;
        char pre = 'a',cur = 'a';
        StringBuilder builder = new StringBuilder();
        while ((d = in.read()) != -1){
            cur = (char)d;
            if(pre == CR && cur == LF){
                break;
            }
            builder.append(cur);
            pre = cur;

        }
        String string = builder.toString().trim();
        return string;
    }


    public String getMethod() {
        return method;
    }

    public String getUri() {
        return uri;
    }

    public String getProtocol() {
        return protocol;
    }

    public String getHeader(String key) {
        return headers.get(key);
    }

}
