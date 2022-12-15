package ru.job4j.todo.controller;

import lombok.AllArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.service.TaskService;

import java.util.Optional;

@Controller
@AllArgsConstructor
@ThreadSafe
public class TaskController {
    private final TaskService taskService;

    @GetMapping("/")
    public String getIndex() {
        return "redirect:/todos";
    }

    @GetMapping("/todos")
    public String getTodos(Model model, @RequestParam(name = "status", required = false) Boolean status) {
        if (status != null) {
            model.addAttribute("tasks", taskService.findByStatus(status));
            return "todos";
        }
        model.addAttribute("tasks", taskService.findAll());
        return "todos";
    }

    @GetMapping("/formAddTask")
    public String addTask() {
        return "addTask";
    }

    @PostMapping("/createTask")
    public String create(@ModelAttribute Task task) {
        taskService.create(task);
        return "redirect:/todos";
    }

    @PostMapping("/selectTask")
    public String getTaskPage(Model model, @ModelAttribute Task task) {
        model.addAttribute("task", task);
        return "taskPage";
    }

    @PostMapping("/updateTask")
    public String update(@ModelAttribute Task task) {
        taskService.replace(task);
        return "redirect:/todos";
    }

    @GetMapping("/formUpdateTask/{id}")
    public String updateTask(@PathVariable int id, Model model) {
        Optional<Task> optTask = taskService.findById(id);
        if (optTask.isPresent()) {
            model.addAttribute("task", optTask.get());
            return "updateTask";
        }
        model.addAttribute("error", "Не найдена задача для редактирования");
        return "error";
    }

    @PostMapping("/deleteTask")
    public String delete(@ModelAttribute Task task) {
        taskService.delete(task.getId());
        return "redirect:/todos";
    }
}
