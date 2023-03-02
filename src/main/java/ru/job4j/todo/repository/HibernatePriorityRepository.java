package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Priority;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class HibernatePriorityRepository implements PriorityRepository {
    private static final String FROM_PRIORITY = "from priorities";
    private static final String FROM_PRIORITY_WHERE_ID = "from priorities where id = :id";
    private final CrudRepository crudRepository;

    @Override
    public Priority create(Priority priority) {
        crudRepository.run(session -> session.save(priority));
        return priority;
    }

    @Override
    public List<Priority> findAll() {
        return crudRepository.query(FROM_PRIORITY, Priority.class);
    }

    @Override
    public Optional<Priority> findById(int id) {
        return crudRepository.optional(FROM_PRIORITY_WHERE_ID, Priority.class, Map.of("id", id));
    }
}
