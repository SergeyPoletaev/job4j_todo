package ru.job4j.todo.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.todo.model.Priority;
import ru.job4j.todo.repository.PriorityRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class PriorityServiceImpl implements PriorityService {
    private final PriorityRepository priorityRepository;

    @Override
    public Priority create(Priority priority) {
        return priorityRepository.create(priority);
    }

    @Override
    public List<Priority> findAll() {
        return priorityRepository.findAll();
    }
}
