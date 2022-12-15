package ru.job4j.todo.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.todo.config.TestHibernateConfig;
import ru.job4j.todo.model.Task;

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
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        }
        session.close();
    }

    @Test
    void create() {
        TaskRepository taskRepository = new HibernateTaskRepository(sf);
        Task task = new Task();
        task.setName("msg");
        task.setDescription("desc");
        Task newTask = taskRepository.create(task);
        assertThat(newTask.getName()).isEqualTo("msg");
    }

    @Test
    void replace() {
        TaskRepository taskRepository = new HibernateTaskRepository(sf);
        Task task = new Task();
        task.setName("msg");
        task.setDescription("desc");
        task = taskRepository.create(task);
        Task updTask = new Task();
        updTask.setId(task.getId());
        updTask.setName("msg1");
        updTask.setDescription("desc");
        assertThat(taskRepository.replace(updTask)).isTrue();
        assertThat(taskRepository.findById(task.getId())
                .orElseThrow()
                .getName())
                .isEqualTo("msg1");
    }

    @Test
    void delete() {
        TaskRepository taskRepository = new HibernateTaskRepository(sf);
        Task task = new Task();
        task.setName("msg");
        task.setDescription("desc");
        task = taskRepository.create(task);
        assertThat(taskRepository.delete(task.getId())).isTrue();
        assertThat(taskRepository.findById(task.getId())).isEqualTo(Optional.empty());
    }

    @Test
    void findById() {
        TaskRepository taskRepository = new HibernateTaskRepository(sf);
        Task task = new Task();
        task.setName("msg");
        task.setDescription("desc");
        task = taskRepository.create(task);
        assertThat(taskRepository.findById(task.getId())).isEqualTo(Optional.of(task));
    }

    @Test
    void findByStatus() {
        TaskRepository taskRepository = new HibernateTaskRepository(sf);
        Task trueTask = new Task();
        trueTask.setName("msg");
        trueTask.setDescription("desc");
        trueTask.setDone(true);
        trueTask = taskRepository.create(trueTask);
        Task falseTask = new Task();
        falseTask.setName("msg");
        falseTask.setDescription("desc");
        taskRepository.create(falseTask);
        assertThat(taskRepository.findByStatus(true)).isEqualTo(List.of(trueTask));
    }

    @Test
    void findAll() {
        TaskRepository taskRepository = new HibernateTaskRepository(sf);
        Task trueTask = new Task();
        trueTask.setName("msg");
        trueTask.setDescription("desc");
        trueTask.setDone(true);
        trueTask = taskRepository.create(trueTask);
        Task falseTask = new Task();
        falseTask.setName("msg");
        falseTask.setDescription("desc");
        falseTask = taskRepository.create(falseTask);
        assertThat(taskRepository.findAll()).isEqualTo(List.of(trueTask, falseTask));
    }
}