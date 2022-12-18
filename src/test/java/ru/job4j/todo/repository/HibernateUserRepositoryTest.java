package ru.job4j.todo.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.todo.config.TestHibernateConfig;
import ru.job4j.todo.model.User;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class HibernateUserRepositoryTest {
    private static SessionFactory sf;

    @BeforeAll
    static void init() {
        sf = new TestHibernateConfig().sf();
    }

    @AfterAll
    static void close() {
        sf.close();
    }

    @AfterEach
    void cleanDb() {
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            session.createQuery("delete from todo_user").executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        }
        session.close();
    }

    @Test
    void create() {
        User user = new User();
        user.setName("anna");
        user.setLogin("login");
        user.setPassword("password");
        UserRepository userRepository = new HibernateUserRepository(sf);
        user = userRepository.create(user);
        Optional<User> addUser = userRepository.findById(user.getId());
        assertThat(addUser.isPresent()).isTrue();
        assertThat(addUser.get().getName()).isEqualTo("anna");
    }

    @Test
    void replace() {
        User user = new User();
        user.setName("anna");
        user.setLogin("login");
        user.setPassword("password");
        UserRepository userRepository = new HibernateUserRepository(sf);
        user = userRepository.create(user);
        User updUser = new User();
        updUser.setId(user.getId());
        updUser.setName("sveta");
        updUser.setLogin(user.getLogin());
        updUser.setPassword(user.getPassword());
        assertThat(userRepository.replace(updUser)).isTrue();
        assertThat(userRepository.findById(updUser.getId())
                .orElseThrow()
                .getName())
                .isEqualTo("sveta");
    }

    @Test
    void delete() {
        User user = new User();
        user.setName("anna");
        user.setLogin("login");
        user.setPassword("password");
        UserRepository userRepository = new HibernateUserRepository(sf);
        user = userRepository.create(user);
        assertThat(userRepository.delete(user.getId())).isTrue();
        assertThat(userRepository.findById(user.getId())).isEqualTo(Optional.empty());
    }

    @Test
    void findById() {
        User user = new User();
        user.setName("anna");
        user.setLogin("login");
        user.setPassword("password");
        UserRepository userRepository = new HibernateUserRepository(sf);
        user = userRepository.create(user);
        assertThat(userRepository.findById(user.getId())).isEqualTo(Optional.of(user));
    }

    @Test
    void findAll() {
        User user1 = new User();
        user1.setName("anna");
        user1.setLogin("login");
        user1.setPassword("password");
        User user2 = new User();
        user2.setName("sveta");
        user2.setLogin("login2");
        user2.setPassword("password");
        UserRepository userRepository = new HibernateUserRepository(sf);
        userRepository.create(user1);
        userRepository.create(user2);
        assertThat(userRepository.findAll()).isEqualTo(List.of(user1, user2));
    }

    @Test
    void findByLoginAndPassword() {
        User user = new User();
        user.setName("anna");
        user.setLogin("login");
        user.setPassword("password");
        UserRepository userRepository = new HibernateUserRepository(sf);
        user = userRepository.create(user);
        assertThat(userRepository.findByLoginAndPassword(user.getLogin(), user.getPassword()))
                .isEqualTo(Optional.of(user));
    }
}