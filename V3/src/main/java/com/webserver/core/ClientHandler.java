package com.webserver.core;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class ClientHandler implements Runnable{
    private Socket socket;

    public ClientHandler(Socket socket){
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            InputStream in = socket.getInputStream();
            int d;
            char pre = 'a',cur = 'a';
            StringBuilder builder = new StringBuilder();
            while ((d = in.read()) != -1){
                cur = (char)d;
                if(pre == 13 && cur == 10){
                    break;
                }
                builder.append(cur);
                pre = cur;

            }
            String string = builder.toString();
            System.out.println(string);

            String method;
            String uri;
            String protocol;

            String[] strings = string.split(" ");
            method = strings[0];
            uri = strings[1];
            protocol = strings[2];

            System.out.println("method: " + method );
            System.out.println("uri: " + uri );
            System.out.println("protocol: " + protocol );


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
