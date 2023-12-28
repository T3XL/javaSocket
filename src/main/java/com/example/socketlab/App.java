package com.example.socketlab;
import com.example.controller.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
public class App extends Application{
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("Login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 600);
        LoginController controller = fxmlLoader.getController();
        stage.setTitle("ChatLogin!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
