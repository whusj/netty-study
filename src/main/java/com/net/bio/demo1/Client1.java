package com.net.bio.demo1;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.Socket;
import java.util.Random;

/**
 * <p>ClassName:</p>
 * <p>Description:</p>
 * <p>Author:Songjiang</p>
 * <p>CreateDate:2018/11/19</p>
 */
@Slf4j
public class Client1 {
    private static String SERVER_HOST = "127.0.0.1";
    private static int PORT = 8888;

    public static void send(String msg){
        BufferedReader in = null;
        PrintWriter out = null;
        Socket socket = null;
        try {
            socket = new Socket(SERVER_HOST,PORT);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()),true);
        }catch (Exception e){
            log.error(e.getMessage());
        }

        out.println("Client1-"+msg);

        try {

            log.info("客户端收到服务端返回值: " + in.readLine());
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
            if(out != null){
                out.close();
            }
            if(socket != null){
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public static void main(String[] args) {
        final Random random = new Random(System.currentTimeMillis());
        new Thread(new Runnable() {
            public void run() {
                while (true) {
                    Client1.send(String.valueOf(random.nextInt()));
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }
}
