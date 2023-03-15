package ru.job4j.todo.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.job4j.todo.model.Category;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.model.User;
import ru.job4j.todo.service.CategoryService;
import ru.job4j.todo.service.PriorityService;
import ru.job4j.todo.service.TaskService;
import ru.job4j.todo.util.HttpHelper;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Controller
@AllArgsConstructor
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;
    private final PriorityService priorityService;
    private final CategoryService categoryService;

    @GetMapping("/todos")
    public String getTodos(Model model,
                           @RequestParam(name = "status", required = false) Boolean status,
                           HttpSession httpSession) {
        HttpHelper.addUserToModel(httpSession, model);
        if (status != null) {
            model.addAttribute("tasks", taskService.findByStatus(status));
            return "/tasks/todos";
        }
        model.addAttribute("tasks", taskService.findAll());
        return "/tasks/todos";
    }

    @GetMapping("/formAdd")
    public String addTask(HttpSession httpSession, Model model) {
        HttpHelper.addUserToModel(httpSession, model);
        model.addAttribute("priorities", priorityService.findAll());
        model.addAttribute("categories", categoryService.findAll());
        return "/tasks/add";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute Task task, HttpSession httpSession, RedirectAttributes redirectAttr,
                         @RequestParam(value = "selected_categories") List<Integer> categoryIds) {
        User user = (User) httpSession.getAttribute("user");
        task.setUser(user);
        ArrayList<Category> categories = new ArrayList<>();
        for (Integer id : categoryIds) {
            Category category = new Category();
            category.setId(id);
            categories.add(category);
        }
        task.setCategories(categories);
        taskService.create(task);
        if (taskService.findById(task.getId()).isPresent()) {
            return "redirect:/tasks/todos";
        }
        redirectAttr.addFlashAttribute("message", "Создать задачу не получилось!");
        return "redirect:/shared/fail";
    }

    @PostMapping("/select")
    public String getTaskPage(Model model, @ModelAttribute Task task, HttpSession httpSession) {
        HttpHelper.addUserToModel(httpSession, model);
        Task taskDb = taskService.findById(task.getId()).orElseThrow();
        task.setPriority(taskDb.getPriority());
        model.addAttribute("task", task);
        model.addAttribute("categories", taskDb.getCategories()
                .stream()
                .map(Category::getName)
                .toList()
        );
        return "/tasks/todo";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute Task task, RedirectAttributes redirectAttr) {
        redirectAttr.addFlashAttribute("message", "Обновить задачу не получилось!");
        String pageSuccess = "redirect:/tasks/todos";
        String pageFail = "redirect:/shared/fail";
        String rsl;
        try {
            rsl = taskService.replace(task) ? pageSuccess : pageFail;
        } catch (NoSuchElementException e) {
            rsl = pageFail;
        }
        return rsl;
    }

    @GetMapping("/formUpdate/{id}")
    public String updateTask(@PathVariable int id, Model model, HttpSession httpSession) {
        HttpHelper.addUserToModel(httpSession, model);
        Optional<Task> optTask = taskService.findById(id);
        if (optTask.isPresent()) {
            model.addAttribute("task", optTask.get());
            model.addAttribute("priorities", priorityService.findAll());
            model.addAttribute("categories", categoryService.findAll());
            return "/tasks/formUpdate";
        }
        model.addAttribute("message", "Не найдена задача для редактирования");
        return "/shared/fail";
    }

    @PostMapping("/delete")
    public String delete(@ModelAttribute Task task, RedirectAttributes redirectAttr) {
        if (taskService.delete(task.getId())) {
            return "redirect:/tasks/todos";
        }
        redirectAttr.addFlashAttribute("message", "Удалить задачу не получилось!");
        return "redirect:/shared/fail";
    }
}
