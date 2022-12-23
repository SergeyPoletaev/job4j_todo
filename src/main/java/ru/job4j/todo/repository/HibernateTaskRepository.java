package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Task;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class HibernateTaskRepository implements TaskRepository {
    private static final String FROM_TASKS = "from tasks";
    private static final String FROM_TASKS_WHERE_ID = "from tasks where id = :id";
    private static final String FROM_TASKS_WHERE_DONE = "from tasks where done = :status";
    private static final String DELETE_FROM_TASKS = "delete from tasks where id = :id";
    private static final String UPDATE_TASKS =
            "update tasks set name = :name, description = :description, modified = :modified, done = :done where id = :id";
    private final CrudRepository crudRepository;

    @Override
    public Task create(Task task) {
        crudRepository.run(session -> session.save(task));
        return task;
    }

    @Override
    public boolean replace(Task task) {
        return crudRepository.query(
                UPDATE_TASKS,
                Map.of(
                        "name", task.getName(),
                        "description", task.getDescription(),
                        "modified", LocalDateTime.now(),
                        "done", task.isDone(),
                        "id", task.getId()
                ));
    }

    @Override
    public boolean delete(int id) {
        return crudRepository.query(DELETE_FROM_TASKS, Map.of("id", id));
    }

    @Override
    public Optional<Task> findById(int id) {
        return crudRepository.optional(FROM_TASKS_WHERE_ID, Task.class, Map.of("id", id));
    }

    @Override
    public List<Task> findByStatus(boolean status) {
        return crudRepository.query(FROM_TASKS_WHERE_DONE, Task.class, Map.of("status", status));
    }

    @Override
    public List<Task> findAll() {
        return crudRepository.query(FROM_TASKS, Task.class);
    }
}
