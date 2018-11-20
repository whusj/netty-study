package com.net.bio.demo1;

import lombok.extern.slf4j.Slf4j;
import sun.rmi.runtime.Log;

import java.io.*;
import java.net.Socket;

/**
 * <p>ClassName:</p>
 * <p>Description:</p>
 * <p>Author:Songjiang</p>
 * <p>CreateDate:2018/11/19</p>
 */
@Slf4j
public class ServerHandler implements Runnable {

    private Socket socket = null;

    public ServerHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        BufferedReader in = null;
        PrintWriter out = null;
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()),true);

            //一次只能读一行in.readLine()所以要while(true)
            while (true){
                String expression;
                String result;
                if((expression = in.readLine()) == null){
                    break;
                }
                log.info("服务器端接收到值: " + expression);
                result = "<" + expression + ">";
                out.print(result);
            }
        }catch (Exception e){
            log.error(e.getMessage());
        }finally {
            if(in != null){
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (out != null){
                out.close();
            }
            if (socket != null){
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
