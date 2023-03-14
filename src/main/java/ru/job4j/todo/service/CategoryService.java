package ru.job4j.todo.service;

import ru.job4j.todo.model.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {

    Category create(Category category);

    List<Category> findAll();

    Optional<Category> findById(int id);
}
