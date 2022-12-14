package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Task;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
@ThreadSafe
public class HibernateTaskRepository implements TaskRepository {
    private static final String FROM_TASKS = "from tasks";
    private static final String FROM_TASKS_WHERE_ID = "from tasks where id = :id";
    private static final String DELETE_FROM_TASKS = "delete from tasks where id = :id";
    private static final String UPDATE_TASKS =
            "update tasks set name = :name, description = :description, modified = :modified, done = :done where id = :id";
    private final SessionFactory sf;

    @Override
    public Task create(Task task) {
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            session.save(task);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        }
        session.close();
        return task;
    }

    @Override
    public boolean replace(Task task) {
        boolean rsl = false;
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            rsl = session.createQuery(UPDATE_TASKS)
                    .setParameter("name", task.getName())
                    .setParameter("description", task.getDescription())
                    .setParameter("modified", LocalDateTime.now())
                    .setParameter("done", task.isDone())
                    .setParameter("id", task.getId())
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
            rsl = session.createQuery(DELETE_FROM_TASKS)
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
    public Optional<Task> findById(int id) {
        Session session = sf.openSession();
        Optional<Task> rsl = session.createQuery(FROM_TASKS_WHERE_ID, Task.class)
                .setParameter("id", id)
                .uniqueResultOptional();
        session.close();
        return rsl;
    }

    @Override
    public List<Task> findByStatus(boolean status) {
        Session session = sf.openSession();
        List<Task> rsl = session.createQuery("from tasks where done = :status", Task.class)
                .setParameter("status", status)
                .list();
        session.close();
        return rsl;
    }

    @Override
    public List<Task> findAll() {
        Session session = sf.openSession();
        List<Task> rsl = session.createQuery(FROM_TASKS, Task.class).list();
        session.close();
        return rsl;
    }
}
