package com.example.socketlab;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class DatabaseConnect {
    private static String URL = "jdbc:mysql://localhost:3306/chatUser";
    private static String USER = "root";
    private static String PASSWORD = "123456";
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
