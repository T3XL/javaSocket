package com.example.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.util.logging.Logger;

public class LoginController {
    private static final Logger logger = Logger.getLogger(LoginController.class.getName());
    @FXML
    private TextField usernameInput;
    @FXML
    private PasswordField passwordInput;
    @FXML
    private Button loginButton;
    @FXML
    private Text loginError;
    private Stage stage;



    @FXML
    private void loginButtonClicked() {
        String username = usernameInput.getText();
        String password = passwordInput.getText();
        usernameInput.clear();
        passwordInput.clear();
        // 执行登录逻辑
        boolean loginSuccess = performLogin(username, password);

        if (loginSuccess) {
            // 登录成功，切换到聊天界面
            stage = (Stage) loginButton.getScene().getWindow();
            stage.close();
            switchToChatScene(username);

        } else {
            loginError.setText("Login failed. Please check your username and password.");
        }
    }

    private boolean performLogin(String username, String password) {
        try(Socket socket = new Socket("127.0.0.1", 8089)){
            //调用Socket对象的getOutputStream方法获得字节输出流对象
            OutputStream os = socket.getOutputStream();
            PrintWriter pw = new PrintWriter(os);
            pw.println(username+"-"+password);
            pw.flush();
            //调用Socket对象的getInputStream方法获得字节输入流对象
            InputStream is = socket.getInputStream();
            BufferedReader br = new BufferedReader(new java.io.InputStreamReader(is));
            String message = br.readLine();
            return Boolean.parseBoolean(message);
        }
        catch(IOException e){
            logger.info("Client Error: " + e.getMessage());
        }
        return true;
    }
    private void switchToChatScene(String username) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/socketlab/Chat.fxml"));
            Scene scene = new Scene(loader.load(), 600, 600);
            ChatController chatController = loader.getController();
            chatController.setChatSocket(username);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
        }
        catch(IOException e){
            logger.info("Client Error: " + e.getMessage());
        }
    }
}

