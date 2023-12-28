package com.example.socketlab;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Verify {
    public static void main(String[] args) {
        boolean state=login("user2", "123456");
        System.out.println(state);
    }

    public static boolean login(String username, String password) {
        try(Connection connection = DatabaseConnect.getConnection()){
            String query = "SELECT password FROM user WHERE username = ?";
            try(PreparedStatement statement = connection.prepareStatement(query)){
                statement.setString(1, username);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        String storedPassword = resultSet.getString("password");
                        if (storedPassword.equals(password)) {
                            System.out.println("登录成功");
                            return true;
                        } else {
                            System.out.println("密码错误");
                            return false;
                        }
                    } else {
                        System.out.println("用户名不存在");
                        return false;
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
