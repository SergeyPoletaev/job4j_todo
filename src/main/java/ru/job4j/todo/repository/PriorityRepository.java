package ru.job4j.todo.repository;

import ru.job4j.todo.model.Priority;

import java.util.List;

public interface PriorityRepository {

    Priority create(Priority priority);

    List<Priority> findAll();
}
