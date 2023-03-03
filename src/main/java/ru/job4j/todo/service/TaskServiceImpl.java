package ru.job4j.todo.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.repository.TaskRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final PriorityService priorityService;

    @Override
    public Task create(Task task) {
        return taskRepository.create(task);
    }

    @Override
    public boolean replace(Task task) {
        if (task.getPriority() == null) {
            task.setPriority(findById(task.getId()).orElseThrow().getPriority());
        } else {
            int priorityId = task.getPriority().getId();
            task.setPriority(priorityService.findById(priorityId).orElseThrow());
        }
        return taskRepository.replace(task);
    }

    @Override
    public boolean delete(int id) {
        return taskRepository.delete(id);
    }

    @Override
    public Optional<Task> findById(int id) {
        return taskRepository.findById(id);
    }

    @Override
    public List<Task> findByStatus(boolean status) {
        return taskRepository.findByStatus(status);
    }

    @Override
    public List<Task> findAll() {
        return taskRepository.findAll();
    }
}
