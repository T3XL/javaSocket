package com.example.thread;

import com.vdurmont.emoji.EmojiParser;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;

public class ReadThread extends Thread{
    private static final Logger logger = Logger.getLogger(ReadThread.class.getName());
    private Socket socket;
    @FXML
    private ListView<String> userList;
    private List<TextArea> chatAreas;
    public ReadThread(Socket socket, List<TextArea> chatAreas, ListView<String> userList) {
        this.socket = socket;
        this.chatAreas = chatAreas;
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
                LocalDateTime now = LocalDateTime.now();
                String message1 = EmojiParser.parseToUnicode(strings[1]);
                for(TextArea chatArea:chatAreas)
                {
                    chatArea.appendText("--"+now.toString()+"--\n");
                    chatArea.appendText(strings[0]+":"+ message1+"\n");
                }
            }
            catch(IOException e)
            {
                logger.info(e.getMessage());
            }
        }
    }
}
