package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Category;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class HibernateCategoryRepository implements CategoryRepository {
    private static final String FROM_CATEGORIES = "from categories";
    private static final String FROM_CATEGORIES_WHERE_ID_ID = "from categories where id = :id";
    private final CrudRepository crudRepository;

    @Override
    public Category create(Category category) {
        crudRepository.run(session -> session.save(category));
        return category;
    }

    @Override
    public List<Category> findAll() {
        return crudRepository.query(FROM_CATEGORIES, Category.class);
    }

    @Override
    public Optional<Category> findById(int id) {
        return crudRepository.optional(FROM_CATEGORIES_WHERE_ID_ID, Category.class, Map.of("id", id));
    }
}
