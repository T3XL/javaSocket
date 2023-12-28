package com.example.controller;
import com.example.thread.ReadThread;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Logger;

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
            chatArea.appendText(username + " : " + str + "\n");
            pw.println(str);
            pw.flush();
        }
        catch (IOException e){
            logger.info(e.getMessage());
        }
    }
}


