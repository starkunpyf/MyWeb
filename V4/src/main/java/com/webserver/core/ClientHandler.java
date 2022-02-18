package com.webserver.core;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
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
            String string = readLine();
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
            //解析消息头
            Map<String, String> headers = new HashMap();
            while (true){
                string = readLine();
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
            for(Map.Entry<String, String> entry : entrySet){
                System.out.println(entry.getKey() + ":" + entry.getValue());
            }
            System.out.println(headers);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private String readLine() throws IOException {
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
        String string = builder.toString().trim();
        return string;
    }
}
