package ru.job4j.todo.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.service.TaskService;

import java.util.NoSuchElementException;

@Controller
@AllArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @GetMapping("/")
    public String getIndex() {
        return "redirect:/todos";
    }

    @GetMapping("/todos")
    public String getIndex(Model model, @RequestParam(name = "status", required = false) Boolean status) {
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
        model.addAttribute("task", taskService.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Не найден объект для редактирования"))
        );
        return "updateTask";
    }

    @PostMapping("/deleteTask")
    public String delete(@ModelAttribute Task task) {
        taskService.delete(task.getId());
        return "redirect:/todos";
    }
}
