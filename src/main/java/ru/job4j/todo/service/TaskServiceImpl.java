package ru.job4j.todo.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.model.User;
import ru.job4j.todo.repository.TaskRepository;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;
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
    public List<Task> findByStatus(boolean status, Model model) {
        List<Task> tasks = taskRepository.findByStatus(status);
        setUserTzInTaskList(model, tasks);
        return tasks;
    }

    @Override
    public List<Task> findAll(Model model) {
        List<Task> tasks = taskRepository.findAll();
        setUserTzInTaskList(model, tasks);
        return tasks;
    }

    private void setUserTzInTaskList(Model model, List<Task> tasks) {
        User user = (User) model.getAttribute("user");
        for (Task task : tasks) {
            String userZoneId = Objects.requireNonNull(user).getTimezone() != null
                    ? user.getTimezone() : String.valueOf(ZoneId.systemDefault());
            task.setCreated(getUserLocalDateTime(task.getCreated(), userZoneId));
            task.setModified(getUserLocalDateTime(task.getModified(), userZoneId));
        }
    }

    private LocalDateTime getUserLocalDateTime(LocalDateTime localDateTime, String userZoneId) {
        return localDateTime.atZone(ZoneId.systemDefault())
                .withZoneSameInstant(ZoneId.of(userZoneId))
                .toLocalDateTime();
    }
}
