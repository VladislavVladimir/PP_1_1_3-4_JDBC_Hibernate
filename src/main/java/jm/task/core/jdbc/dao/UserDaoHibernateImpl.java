package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class UserDaoHibernateImpl implements UserDao {

    public UserDaoHibernateImpl() {
    }

    private void executeTransaction(Consumer<Session> action) {
        Transaction transaction = null;
        try (Session session = Util.getSession()) {
            try {
                transaction = session.beginTransaction();
                action.accept(session); // Выполняем переданное действие
                transaction.commit();
            } catch (Exception e) {
                if (transaction != null) transaction.rollback();
                System.out.println("Ошибка при выполнении транзакции:");
            }
        } catch (Exception e) {
            System.out.println("Ошибка при получении сессии:");
        }
    }

    @Override
    public void createUsersTable() {
        String sql = "CREATE TABLE IF NOT EXISTS users (id BIGINT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(50), lastName VARCHAR(50), age TINYINT)";
        executeTransaction(session -> session.createNativeQuery(sql).executeUpdate());
    }

    @Override
    public void dropUsersTable() {
        String sql = "DROP TABLE IF EXISTS users";
        executeTransaction(session -> session.createNativeQuery(sql).executeUpdate());
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        executeTransaction(session -> {
            User user = new User(null, name, lastName, age);
            session.save(user);
            System.out.printf("User  с именем — %s добавлен в базу данных%n", name);
        });
    }

    @Override
    public void removeUserById(long id) {
        executeTransaction(session -> {
            User user = session.get(User.class, id);
            if (user != null) {
                session.delete(user);
                System.out.printf("User  с id %d удален из базы данных%n", id);
            } else {
                System.out.printf("User  с id %d не найден%n", id);
            }
        });

    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();

        try (Session session = Util.getSession()) {
            users = session.createQuery("FROM User", User.class).list();
        } catch (Exception e) {
            System.out.println("Не удалось вывести всех пользователей");
        }
        return users;
    }

    @Override
    public void cleanUsersTable() {
        executeTransaction(session -> {
            int deletedRecords = session.createQuery("DELETE FROM User").executeUpdate();
            System.out.printf("Удалено записей: %d%n", deletedRecords);
        });
    }
}