package ru.job4j.todo.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.todo.config.TestHibernateConfig;
import ru.job4j.todo.model.Priority;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.model.User;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class HibernateTaskRepositoryTest {
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
            session.createQuery("delete from tasks").executeUpdate();
            session.createQuery("delete from todo_user").executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        }
        session.close();
    }

    @Test
    void create() {
        TaskRepository taskRepository = new HibernateTaskRepository(new CrudRepositoryImpl(sf));
        UserRepository userRepository = new HibernateUserRepository(new CrudRepositoryImpl(sf));
        Task task = new Task();
        task.setName("msg");
        task.setDescription("desc");
        User user = new User(1, "a", "b", "c");
        user = userRepository.create(user);
        task.setUser(user);
        task.setPriority(new Priority(1, "a", 1));
        task = taskRepository.create(task);
        Optional<Task> addTask = taskRepository.findById(task.getId());
        assertThat(addTask.isPresent()).isTrue();
        assertThat(addTask.get().getName()).isEqualTo("msg");
    }

    @Test
    void replace() {
        TaskRepository taskRepository = new HibernateTaskRepository(new CrudRepositoryImpl(sf));
        UserRepository userRepository = new HibernateUserRepository(new CrudRepositoryImpl(sf));
        Task task = new Task();
        task.setName("msg");
        task.setDescription("desc");
        User user = new User(1, "a", "b", "c");
        user = userRepository.create(user);
        task.setUser(user);
        task.setPriority(new Priority(1, "a", 1));
        task = taskRepository.create(task);
        Task updTask = new Task();
        updTask.setId(task.getId());
        updTask.setName("msg1");
        updTask.setDescription("desc");
        updTask.setPriority(new Priority(2, "b", 2));
        assertThat(taskRepository.replace(updTask)).isTrue();
        assertThat(taskRepository.findById(task.getId())
                .orElseThrow()
                .getName())
                .isEqualTo("msg1");
    }

    @Test
    void delete() {
        TaskRepository taskRepository = new HibernateTaskRepository(new CrudRepositoryImpl(sf));
        UserRepository userRepository = new HibernateUserRepository(new CrudRepositoryImpl(sf));
        Task task = new Task();
        task.setName("msg");
        task.setDescription("desc");
        User user = new User(1, "a", "b", "c");
        user = userRepository.create(user);
        task.setUser(user);
        task.setPriority(new Priority(1, "a", 1));
        task = taskRepository.create(task);
        assertThat(taskRepository.delete(task.getId())).isTrue();
        assertThat(taskRepository.findById(task.getId())).isEqualTo(Optional.empty());
    }

    @Test
    void findById() {
        TaskRepository taskRepository = new HibernateTaskRepository(new CrudRepositoryImpl(sf));
        UserRepository userRepository = new HibernateUserRepository(new CrudRepositoryImpl(sf));
        Task task = new Task();
        task.setName("msg");
        task.setDescription("desc");
        User user = new User(1, "a", "b", "c");
        user = userRepository.create(user);
        task.setUser(user);
        task.setPriority(new Priority(1, "a", 1));
        task = taskRepository.create(task);
        assertThat(taskRepository.findById(task.getId())).isEqualTo(Optional.of(task));
    }

    @Test
    void findByStatus() {
        TaskRepository taskRepository = new HibernateTaskRepository(new CrudRepositoryImpl(sf));
        UserRepository userRepository = new HibernateUserRepository(new CrudRepositoryImpl(sf));
        Task trueTask = new Task();
        trueTask.setName("msg");
        trueTask.setDescription("desc");
        trueTask.setPriority(new Priority(1, "a", 1));
        User user = new User(1, "a", "b", "c");
        user = userRepository.create(user);
        trueTask.setUser(user);
        trueTask.setDone(true);
        trueTask = taskRepository.create(trueTask);
        Task falseTask = new Task();
        falseTask.setName("msg");
        falseTask.setDescription("desc");
        falseTask.setUser(user);
        falseTask.setPriority(new Priority(1, "a", 1));
        taskRepository.create(falseTask);
        assertThat(taskRepository.findByStatus(true)).isEqualTo(List.of(trueTask));
    }

    @Test
    void findAll() {
        TaskRepository taskRepository = new HibernateTaskRepository(new CrudRepositoryImpl(sf));
        UserRepository userRepository = new HibernateUserRepository(new CrudRepositoryImpl(sf));
        Task trueTask = new Task();
        trueTask.setName("msg");
        trueTask.setDescription("desc");
        trueTask.setDone(true);
        trueTask.setPriority(new Priority(1, "a", 1));
        User user = new User(1, "a", "b", "c");
        user = userRepository.create(user);
        trueTask.setUser(user);
        trueTask = taskRepository.create(trueTask);
        Task falseTask = new Task();
        falseTask.setName("msg");
        falseTask.setDescription("desc");
        falseTask.setUser(user);
        falseTask.setPriority(new Priority(1, "a", 1));
        falseTask = taskRepository.create(falseTask);
        assertThat(taskRepository.findAll()).isEqualTo(List.of(trueTask, falseTask));
    }
}