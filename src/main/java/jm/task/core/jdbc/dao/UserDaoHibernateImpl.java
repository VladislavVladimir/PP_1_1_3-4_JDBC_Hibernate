package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;

public class UserDaoHibernateImpl implements UserDao {

    public UserDaoHibernateImpl() {
    }

    private void executeUpdate(String sql) {
        Transaction transaction = null;

        try (Session session = Util.getSession()) {
            transaction = session.beginTransaction();
            session.createNativeQuery(sql).executeUpdate();
            //session.createSQLQuery(sql).addEntity(User.class).executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            System.out.println("Не удалось выполнить sql запрос над данной таблицей");
        }
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
        Transaction transaction = null;
        try (Session session = Util.getSession()) {
            transaction = session.beginTransaction();
            User user = new User(null, name, lastName, age);
            session.save(user);
            transaction.commit();
            System.out.printf("User с именем — %s добавлен в базу данных%n", name);
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            System.out.println("Не удалось добавить пользователя");
        }
    }

    @Override
    public void removeUserById(long id) {
        Transaction transaction = null;
        try (Session session = Util.getSession()) {
            transaction = session.beginTransaction();
            User user = session.get(User.class, id);
            if (user != null) {
                session.delete(user);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            System.out.println("Не удалось удалить пользователя");
        }

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
        Transaction transaction = null;
        String hql = "DELETE FROM User";

        try (Session session = Util.getSession()) {
            transaction = session.beginTransaction();
            session.createQuery(hql).executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            System.out.println("Не удалось очистить таблицу");
        }
    }
}
