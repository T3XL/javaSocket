package com.example.socketlab;

import com.example.thread.ReadThread;
import com.example.thread.WriteThread;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Logger;

public class Client {
    private static final Logger logger = Logger.getLogger(Client.class.getName());
    public static void main(String[] args){
        try(Socket socket = new Socket("127.0.0.1", 8089)){
            Scanner sc=new Scanner(System.in);
            System.out.println("输入用户名");
            String username=sc.nextLine();
            System.out.println("输入密码");
            String password=sc.nextLine();
            //调用Socket对象的getOutputStream方法获得字节输出流对象
            OutputStream os = socket.getOutputStream();
            PrintWriter pw = new PrintWriter(os);
            pw.println(username+"-"+password);
            pw.flush();
            //调用Socket对象的getInputStream方法获得字节输入流对象
            InputStream is = socket.getInputStream();
            BufferedReader br = new BufferedReader(new java.io.InputStreamReader(is));
            String message = br.readLine();
            if(Boolean.parseBoolean(message))
            {
                System.out.println("登录成功");
                chat(username);
            }
        }
        catch(IOException e){
            logger.info("Client Error: " + e.getMessage());
        }
    }

    private static void chat(String username) {
        try{
            Socket chatSocket = new Socket("localhost", 8088);
            OutputStream os = chatSocket.getOutputStream();
            PrintWriter pw = new PrintWriter(os);
            pw.println(username);
            pw.flush();

            new WriteThread(chatSocket).start();
            new ReadThread(chatSocket).start();
        }
        catch(IOException e){
            logger.info("Client Error: " + e.getMessage());
        }
    }
}
