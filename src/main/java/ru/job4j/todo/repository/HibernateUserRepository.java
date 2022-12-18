package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.User;

import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class HibernateUserRepository implements UserRepository {
    private static final String UPDATE_USER = "update todo_user set name = :name, password = :password where id = :id";
    private static final String DELETE_USER = "delete from todo_user where id = :id";
    private static final String FROM_USER_WHERE_ID = "from todo_user where id = :id";
    private static final String SELECT_ALL_USER = "from todo_user";
    private static final String FROM_USER_WHERE_LOGIN_AND_PASSWORD
            = "from todo_user where login = :login and password = :password";
    private final SessionFactory sf;

    @Override
    public User create(User user) {
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            session.save(user);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        }
        session.close();
        return user;
    }

    @Override
    public boolean replace(User user) {
        boolean rsl = false;
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            rsl = session.createQuery(UPDATE_USER)
                    .setParameter("name", user.getName())
                    .setParameter("password", user.getPassword())
                    .setParameter("id", user.getId())
                    .executeUpdate() > 0;
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        }
        session.close();
        return rsl;
    }

    @Override
    public boolean delete(int id) {
        boolean rsl = false;
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            rsl = session.createQuery(DELETE_USER)
                    .setParameter("id", id)
                    .executeUpdate() > 0;
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        }
        session.close();
        return rsl;
    }

    @Override
    public Optional<User> findById(int id) {
        Session session = sf.openSession();
        Optional<User> rsl = session.createQuery(FROM_USER_WHERE_ID, User.class)
                .setParameter("id", id)
                .uniqueResultOptional();
        session.close();
        return rsl;
    }

    @Override
    public List<User> findAll() {
        Session session = sf.openSession();
        List<User> rsl = session.createQuery(SELECT_ALL_USER, User.class).list();
        session.close();
        return rsl;
    }

    @Override
    public Optional<User> findByLoginAndPassword(String login, String password) {
        Session session = sf.openSession();
        Optional<User> rsl = session.createQuery(FROM_USER_WHERE_LOGIN_AND_PASSWORD, User.class)
                .setParameter("login", login)
                .setParameter("password", password)
                .uniqueResultOptional();
        session.close();
        return rsl;
    }
}
