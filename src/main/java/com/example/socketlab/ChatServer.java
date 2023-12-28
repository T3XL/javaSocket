package com.example.socketlab;

import com.example.thread.ChatThread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class ChatServer {
    private static final Logger logger = Logger.getLogger(ChatServer.class.getName());
    public static Map<String, Socket> userMap = new HashMap<>();
    public static void main(String[] args) {
        try(ServerSocket serverSocket = new ServerSocket(8088))
        {
            while(true)
            {
                System.out.println("聊天服务器启动");
                Socket socket = serverSocket.accept();
                System.out.println("连接成功");

                InputStream is = socket.getInputStream();
                BufferedReader br = new BufferedReader(new java.io.InputStreamReader(is));
                String username = br.readLine();
                userMap.put(username, socket);
                System.out.println("用户"+username+"已连接");
                new ChatThread(socket,username).start();
            }
        }
        catch (IOException e)
        {
            logger.severe(e.getMessage());
        }
    }
}
