package com.net.bio.demo1;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * <p>ClassName:</p>
 * <p>Description:</p>
 * <p>Author:Songjiang</p>
 * <p>CreateDate:2018/11/19</p>
 */
@Slf4j
public class Server {

    private static int PORT = 8888;
    private static ServerSocket serverSocket = null;

    public static void start() throws IOException {
        try {
            serverSocket = new ServerSocket(PORT);
            log.info("服务已启动,端口为: " + PORT);
            while (true){
                Socket socket = serverSocket.accept();//阻塞
                new Thread(new ServerHandler(socket)).start();
            }
        }catch (Exception e){
            log.error(e.getMessage());
        }finally {
            if(serverSocket != null){
                serverSocket.close();
            }
            serverSocket = null;//加快GC回收
        }
    }

    public static void main(String[] args) throws IOException {
        Server.start();
    }
}
