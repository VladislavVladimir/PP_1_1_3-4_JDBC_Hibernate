package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
    private Session session;

    public UserDaoHibernateImpl() {
        updateSession();
    }

    public void updateSession() {
        if (session != null) {
            session.close();
        }
        session = Util.getSession();
    }

    private void executeUpdate(String sql) {
        session.beginTransaction();
        //session.createSQLQuery(sql).executeUpdate();
        session.createNativeQuery(sql).executeUpdate();
        session.getTransaction().commit();
    }

    @Override
    public void createUsersTable() {
        String sql = "CREATE TABLE IF NOT EXISTS users (id BIGINT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(50), lastName VARCHAR(50), age TINYINT)";
        executeUpdate(sql);
    }

    @Override
    public void dropUsersTable() {
        String sql = "DROP TABLE IF EXISTS users";
        executeUpdate(sql);
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        try {
            session.beginTransaction();
            User user = new User(null, name, lastName, age);
            session.save(user);
            session.getTransaction().commit();
            System.out.printf("User с именем — %s добавлен в базу данных%n", name);
        } catch (Exception e) {
            if (session.getTransaction() != null) session.getTransaction().rollback();
            System.out.println("Не удалось добавить пользователя");
        }
    }

    @Override
    public void removeUserById(long id) {
        try {
            session.beginTransaction();
            User user = session.get(User.class, id);
            if (user != null) {
                session.delete(user);
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            if (session.getTransaction() != null) session.getTransaction().rollback();
            System.out.println("Не удалось удалить пользователя");
        }

    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();

        try {
            users = session.createQuery("FROM User", User.class).list();
        } catch (Exception e) {
            if (session.getTransaction() != null) session.getTransaction().rollback();
            System.out.println("Не удалось вывести всех пользователей");
        }
        return users;
    }

    @Override
    public void cleanUsersTable() {
        String sql = "DELETE FROM users";
        try {
            executeUpdate(sql);
        } catch (Exception e) {
            if (session.getTransaction() != null) session.getTransaction().rollback();
            System.out.println("Не удалось очистить таблицу");
        }
    }
}
