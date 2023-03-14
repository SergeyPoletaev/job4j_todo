package ru.job4j.todo.repository;

import ru.job4j.todo.model.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository {

    Category create(Category category);

    List<Category> findAll();

    Optional<Category> findById(int id);
}
