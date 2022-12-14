package ru.job4j.todo.service;

import ru.job4j.todo.model.Task;

import java.util.List;
import java.util.Optional;

public interface TaskService {

    Task create(Task task);

    boolean replace(Task task);

    boolean delete(int id);

    Optional<Task> findById(int id);

    List<Task> findByStatus(boolean status);

    List<Task> findAll();
}
