package com.example.thread;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.logging.Logger;

public class ReadThread extends Thread{
    private static final Logger logger = Logger.getLogger(ReadThread.class.getName());
    private Socket socket;
    @FXML
    private TextArea chatArea;
    @FXML
    private ListView<String> userList;
    public ReadThread(Socket socket, TextArea chatArea, ListView<String> userList) {
        this.socket = socket;
        this.chatArea = chatArea;
        this.userList = userList;
    }
    @Override
    public void run() {
        while(true)
        {
            try{
                InputStream is = socket.getInputStream();
                BufferedReader br = new BufferedReader(new java.io.InputStreamReader(is));
                String message = br.readLine();
                String[] strings = message.split(":",2);
                if(!userList.getItems().contains(strings[0]))
                {
                    userList.getItems().add(strings[0]);
                }
                chatArea.appendText(message+"\n");
            }
            catch(IOException e)
            {
                logger.info(e.getMessage());
            }
        }
    }
}
