package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Priority;

import java.util.List;

@Repository
@AllArgsConstructor
public class HibernatePriorityRepository implements PriorityRepository {
    private static final String FROM_PRIORITY = "from priorities";
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
}
