package com.example.thread;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Logger;

public class WriteThread extends Thread{
    private static final Logger logger=Logger.getLogger(WriteThread.class.getName());
    private Socket socket;
    public WriteThread(Socket socket) {

        this.socket = socket;
    }
    @Override
    public void run() {
        while(true){
            try{
                OutputStream os = socket.getOutputStream();
                PrintWriter pw = new PrintWriter(os);
                Scanner sc=new Scanner(System.in);
                System.out.println("请输入内容：");
                String str=sc.nextLine();
                pw.println(str);
                pw.flush();
            }
            catch (IOException e){
                logger.info(e.getMessage());
            }
        }
    }
}
