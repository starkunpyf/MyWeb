package com.webserver.http;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class HttpServletPesponse {
    private int statusCode = 200;
    private String statusReason = "OK";

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
        String line;
        line = "Content-Type: text/html";
        println(line);
        System.out.println("发送响应头："+line);

        line = "Content-Length: "+contentFile.length();
        println(line);
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

}
