package com.example.socketlab;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

public class LoginServer {
    private static final Logger logger=Logger.getLogger(LoginServer.class.getName());
    public static void main(String[] args) {
        try(ServerSocket server=new ServerSocket(8089)){
            while (true){
                System.out.println("Server is running");
                Socket socket=server.accept();
                System.out.println("Client is connected");

                //调用Socket对象的getInputStream方法获得字节输入流对象
                InputStream is = socket.getInputStream();
                BufferedReader br = new BufferedReader(new java.io.InputStreamReader(is));
                String message = br.readLine();
                String[] split = message.split("-");
                String username = split[0];
                String password = split[1];
                boolean state = Verify.login(username, password);
                //调用Socket对象的getOutputStream方法获得字节输出流对象
                OutputStream os = socket.getOutputStream();
                PrintWriter pw = new PrintWriter(os);
                pw.println(state);
                pw.flush();
            }
        }
        catch(IOException e){
            logger.info(e.getMessage());
        }
    }
}
