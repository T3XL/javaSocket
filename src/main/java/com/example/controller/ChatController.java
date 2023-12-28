package com.example.controller;
import com.example.socketlab.DatabaseConnect;
import com.example.thread.ReadThread;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Logger;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;

public class ChatController {
    @FXML
    private ListView<String> userList;
    @FXML
    private TextArea chatArea;
    @FXML
    private TextField messageInput;

    private Socket socket;
    private String username;
    private static final Logger logger = Logger.getLogger(ChatController.class.getName());
    @FXML
    private void initialize() {
        try(Connection connection = DatabaseConnect.getConnection()){
            String sql = "SELECT * FROM chat_log";
            try (PreparedStatement statement = connection.prepareStatement(sql);
                 ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    LocalDateTime timestamp = resultSet.getTimestamp("timestamp").toLocalDateTime();
                    String username = resultSet.getString("username");
                    String content = resultSet.getString("content");
                    chatArea.appendText("--"+timestamp.toString()+"--\n");
                    chatArea.appendText(username + ":" + content + "\n");
                }
            }
        }
        catch(SQLException e)
        {
            logger.info("Chat Controller Initialization Error: " + e.getMessage());
        }
    }
    public void setChatSocket(String username) {
        try {
            Socket chatSocket = new Socket("localhost", 8088);
            this.socket = chatSocket;
            this.username = username;
            OutputStream os = chatSocket.getOutputStream();
            PrintWriter pw = new PrintWriter(os);
            pw.println(username);
            pw.flush();

            new ReadThread(chatSocket, chatArea, userList).start();
        } catch (IOException e) {
            logger.info("Chat Controller Initialization Error: " + e.getMessage());
        }
    }
    @FXML
    private void sendButtonClicked(){
        try{
            OutputStream os = socket.getOutputStream();
            PrintWriter pw = new PrintWriter(os);
            String str = messageInput.getText();
            messageInput.clear();
            LocalDateTime now = LocalDateTime.now();
            chatArea.appendText("--"+now.toString()+"--\n");
            chatArea.appendText(username + " : " + str + "\n");
            pw.println(str);
            pw.flush();
        }
        catch (IOException e){
            logger.info(e.getMessage());
        }
    }
}


