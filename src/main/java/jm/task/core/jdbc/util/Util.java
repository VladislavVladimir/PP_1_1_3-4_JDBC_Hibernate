package jm.task.core.jdbc.util;

import jm.task.core.jdbc.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;

public class Util {
    private final static String URL = "jdbc:mysql://localhost:3306/mytestdb";
    private final static String USERNAME = "root";
    private final static String PASSWORD = "root";

    // Статические переменные для хранения соединений
    private static Connection connection;
    private static SessionFactory sessionFactory;
    private static Session session;

    // реализуйте настройку соединения с БД
    public static Connection getConnection() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            } catch (Exception sqlEx) {
                System.out.println("Не удалось подключиться к базе данных");
            }
        }
        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (Exception e) {
                System.out.println("Не удалось закрыть соединение");
            }
        }
    }

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            Configuration config = new Configuration();
            config.setProperty("hibernate.connection.driver_class", "com.mysql.jdbc.Driver");
            config.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
            config.setProperty("hibernate.connection.url", URL);
            config.setProperty("hibernate.connection.username", USERNAME);
            config.setProperty("hibernate.connection.password", PASSWORD);
            config.setProperty("hibernate.show_sql", "true");
            //config.setProperty("hibernate.hbm2ddl.auto", "create");
            config.setProperty("hibernate.format_sql", "true");
            config.addAnnotatedClass(User.class);

            sessionFactory = config.buildSessionFactory();
        }
        return sessionFactory;
    }

    public static Session getSession() {
        if (session == null) {
            session = getSessionFactory().openSession();
        }
        return session;
    }

    public static void closeSession() {
        if (session != null) {
            session.close();
        }
    }

    public static void closeSessionFactory() {
        closeSession();
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

    public static void closeAll() {
        closeSessionFactory();
        closeConnection();
    }
}
