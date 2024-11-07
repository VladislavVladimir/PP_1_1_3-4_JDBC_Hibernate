package jm.task.core.jdbc.util;

import java.sql.*;

public class Util {
    private final static String URL = "jdbc:mysql://localhost:3306/mytestdb";
    private final static String USERNAME = "root";
    private final static String PASSWORD = "root";

    // Статическая переменная для хранения соединения
    private static Connection connection;

    // реализуйте настройку соединения с БД
    public static Connection getConnection() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            } catch (SQLException sqlEx) {
                System.out.println("Не удалось подключиться к базе данных");
            }
        }
        return connection;
    }


    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.out.println("Не удалось закрыть соединение");
            }
        }
    }

}
