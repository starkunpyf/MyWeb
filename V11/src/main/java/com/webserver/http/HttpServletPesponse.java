package com.webserver.http;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class HttpServletPesponse {
    //状态行
    private int statusCode = 200;
    private String statusReason = "OK";
    //响应头
    private Map<String,String> headers = new HashMap<String,String>();

    //响应正文
    private File contentFile;
    private Socket socket;

    public HttpServletPesponse(Socket socket) {
        this.socket = socket;
    }

    public void Response() throws IOException {
        sendStatusLine();
        sendHeaders();
        sendContent();

    }

    private void sendStatusLine() throws IOException {
        //3.1发送状态行
        String line = "HTTP/1.1"+" "+statusCode+" "+statusReason;
        println(line);
        System.out.println("发送状态行："+line);
    }
    private void sendHeaders() throws IOException {
        //3.2发送响应头
        Set<Map.Entry<String, String>> entrySet = headers.entrySet();
        for(Map.Entry<String, String> entry : entrySet){
            String name = entry.getKey();
            String value = entry.getValue();
            String line = name + ": "+value;
            println(line);
            System.out.println("响应头: "+line);
        }

        //单独发送回车+换行表示响应头部分发送完毕
        println("");
    }
    private void sendContent() throws IOException {
        //3.3发送响应正文
        OutputStream out = socket.getOutputStream();
        byte[] buf = new byte[1024*10];
        int len;
        try(
                FileInputStream fis = new FileInputStream(contentFile);
                ) {
            while((len = fis.read(buf))!=-1){
                out.write(buf,0,len);
            }
        }

        System.out.println("响应发送完毕");

    }

    private void println(String line) throws IOException {
        OutputStream out = socket.getOutputStream();
        byte[] data = line.getBytes(StandardCharsets.ISO_8859_1);
        out.write(data);
        out.write(13);//发送回车符
        out.write(10);//发送换行符
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusReason() {
        return statusReason;
    }

    public void setStatusReason(String statusReason) {
        this.statusReason = statusReason;
    }

    public File getContentFile() {
        return contentFile;
    }

    public void setContentFile(File contentFile) {
        this.contentFile = contentFile;
    }

    /**
     *
     * @param name
     * @param value
     */
    public void addHeader(String name, String value) {
        this.headers.put(name, value);
    }
}
