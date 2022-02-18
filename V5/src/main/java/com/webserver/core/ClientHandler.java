package com.webserver.core;

import com.webserver.http.HttpServletRequst;

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
            HttpServletRequst requst = new HttpServletRequst(socket);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
