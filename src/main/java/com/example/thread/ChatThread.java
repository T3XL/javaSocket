package com.example.thread;
import com.example.socketlab.ChatServer;
import com.example.socketlab.DatabaseConnect;

import java.io.*;
import java.net.Socket;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import java.sql.PreparedStatement;
import java.time.LocalDateTime;

public class ChatThread extends Thread{
    private static final Logger logger = Logger.getLogger(ChatThread.class.getName());
    private Socket socket;
    private String username;
    public ChatThread(Socket socket,String username) {
        this.socket = socket;
        this.username = username;
    }
    @Override
    public void run() {
        while(true)
        {
            try{
                InputStream is = socket.getInputStream();
                BufferedReader br = new BufferedReader(new java.io.InputStreamReader(is));
                String message = br.readLine();
                String[] value = message.split("-",2);
                if(value.length == 1)
                {
                    saveMessage(username,message);
                    Map<String,Socket> userMap= ChatServer.userMap;
                    Set<String> keySet = userMap.keySet();
                    for(String key : keySet)
                    {
                        Socket s =userMap.get(key);
                        if(s != this.socket)
                        {
                            OutputStream os = s.getOutputStream();
                            PrintWriter pw = new PrintWriter(os);
                            pw.println(username+":"+message);
                            pw.flush();
                        }
                    }
                }else{
                    saveMessage(username,value[1]);
                    Map<String,Socket> userMap= ChatServer.userMap;
                    Socket s =userMap.get(value[0]);
                    OutputStream os = s.getOutputStream();
                    PrintWriter pw = new PrintWriter(os);
                    pw.println(username+":"+value[1]);
                    pw.flush();
                }
            }
            catch(IOException e)
            {
                logger.info(e.getMessage());
            }
        }
    }

    private void saveMessage(String username, String message) {
        try(Connection connection = DatabaseConnect.getConnection()){
            String sql = "INSERT INTO chat_log (timestamp, username, content) VALUES (?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                // 设置参数
                statement.setTimestamp(1, java.sql.Timestamp.valueOf(LocalDateTime.now()));
                statement.setString(2, username);
                statement.setString(3, message);
                // 执行插入
                statement.executeUpdate();
            }
        }
        catch(SQLException e)
        {
            logger.info(e.getMessage());
        }
    }
}
