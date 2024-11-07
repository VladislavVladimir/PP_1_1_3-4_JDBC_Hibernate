package jm.task.core.jdbc;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;
import jm.task.core.jdbc.util.Util;

import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        // реализуйте алгоритм здесь
        UserService userService = new UserServiceImpl();

        userService.createUsersTable();

        userService.saveUser ("Kirill", "Petrov", (byte) 21);
        userService.saveUser ("boolean", "Boolean", (byte) 32);
        userService.saveUser ("Char", "Character", (byte) 23);
        userService.saveUser ("Int", "Integer", (byte) 27);

        System.out.println("Все пользователи из базы:");
        for (User user : userService.getAllUsers()) {
            System.out.println(user);
        }

        userService.cleanUsersTable();
        userService.dropUsersTable();

        Util.closeConnection();
    }
}
