package com.example.controller;
import com.example.socketlab.DatabaseConnect;
import com.example.thread.ReadThread;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Logger;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import com.vdurmont.emoji.EmojiParser;
import javafx.scene.control.TextArea;

import java.util.List;


public class ChatController {
    @FXML
    private TabPane tabPane;
    @FXML
    private ListView<String> userList;
    @FXML
    private TextArea chatArea;
    @FXML
    private TextArea messageInput;
    @FXML
    private ChoiceBox<String> emojiChoiceBox;

    private String selectedUser;
    private Socket socket;
    private String username;
    private List<TextArea> chatAreas = new ArrayList<>();
    private static final Logger logger = Logger.getLogger(ChatController.class.getName());
    @FXML
    private void initialize() {
        try(Connection connection = DatabaseConnect.getConnection()){
            userList.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2) { // 双击触发私聊
                    String selectedUser = userList.getSelectionModel().getSelectedItem();
                    if (selectedUser != null) {
                        startPrivateChat(selectedUser);
                    }
                }
            });
            chatAreas.add(chatArea);
            String sql = "SELECT * FROM chat_log";
            try (PreparedStatement statement = connection.prepareStatement(sql);
                 ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    LocalDateTime timestamp = resultSet.getTimestamp("timestamp").toLocalDateTime();
                    String username = resultSet.getString("username");
                    String content = resultSet.getString("content");
                    chatArea.appendText("--"+timestamp.toString()+"--\n");
                    String content2 = EmojiParser.parseToUnicode(content);
                    chatArea.appendText(username + ":" + content2 + "\n");
//                    if(!userList.getItems().contains(username))
//                    {
//                        userList.getItems().add(username);
//                    }
                }
            }
        }
        catch(SQLException e)
        {
            logger.info("Chat Controller Initialization Error: " + e.getMessage());
        }
    }
//    @FXML
//    private void groupChatSelected(){
//        System.out.println("aaaa");
//    }
//    onSelectionChanged="#groupChatSelected"

    private void handlePrivateChatSelection(String selectedUser) {
        this.selectedUser = selectedUser;
    }
    private void startPrivateChat(String selectedUser) {
        Tab privateChatTab = new Tab("Private Chat with " + selectedUser);
        TextArea privateChatArea = new TextArea();
        privateChatTab.setContent(privateChatArea);

        chatAreas.add(privateChatArea);
        // 添加Tab到TabPane
        tabPane.getTabs().add(privateChatTab);

        privateChatTab.setOnSelectionChanged(event -> {
            if (privateChatTab.isSelected()) {
                handlePrivateChatSelection(selectedUser);
            }
        });
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

            new ReadThread(chatSocket, chatAreas, userList).start();
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
            String str2 = EmojiParser.parseToUnicode(str);
            messageInput.clear();
            LocalDateTime now = LocalDateTime.now();
            for(TextArea chatArea:chatAreas)
            {
                chatArea.appendText("--"+now.toString()+"--\n");
                chatArea.appendText(username + ":" + str2 + "\n");
            }
            pw.println(str);
            pw.flush();
        }
        catch (IOException e){
            logger.info(e.getMessage());
        }
    }
    @FXML
    private void psendButtonClicked(){
        try{
            OutputStream os = socket.getOutputStream();
            PrintWriter pw = new PrintWriter(os);
            String str = messageInput.getText();
            String str2 = EmojiParser.parseToUnicode(str);
            messageInput.clear();
            LocalDateTime now = LocalDateTime.now();
            for(TextArea chatArea:chatAreas)
            {
                chatArea.appendText("--"+now.toString()+"--\n");
                chatArea.appendText(username + ":" + str2 + "\n");
            }
            pw.println(selectedUser+"-"+str);
            pw.flush();
        }
        catch (IOException e){
            logger.info(e.getMessage());
        }
    }
    @FXML
    private void emojiSelected(){
        String selectedEmoji = emojiChoiceBox.getValue();
        insertTextAtCaret(messageInput, selectedEmoji);
    }
    private void insertTextAtCaret(TextArea textArea, String text) {
        int caretPosition = textArea.getCaretPosition();
        String currentText = textArea.getText();
        String newText = currentText.substring(0, caretPosition) + text + currentText.substring(caretPosition);
        textArea.setText(newText);
        textArea.positionCaret(caretPosition + text.length()); // 移动光标到插入后的位置
    }
}


