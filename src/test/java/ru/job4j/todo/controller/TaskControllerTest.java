package ru.job4j.todo.controller;

import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.service.TaskService;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class TaskControllerTest {

    @Test
    void getIndex() {
        TaskService taskService = mock(TaskService.class);
        TaskController controller = new TaskController(taskService);
        String page = controller.getIndex();
        assertThat(page).isEqualTo("redirect:/todos");
    }

    @Test
    void whenGetTodosParamFree() {
        List<Task> tasks = List.of(new Task(), new Task());
        TaskService taskService = mock(TaskService.class);
        when(taskService.findAll()).thenReturn(tasks);
        TaskController controller = new TaskController(taskService);
        Model model = mock(Model.class);
        String page = controller.getTodos(model, null);
        verify(model).addAttribute("tasks", tasks);
        assertThat(page).isEqualTo("todos");
    }

    @Test
    void whenGetTodosWithParam() {
        List<Task> tasks = List.of(new Task(), new Task());
        TaskService taskService = mock(TaskService.class);
        boolean status = false;
        when(taskService.findByStatus(status)).thenReturn(tasks);
        TaskController controller = new TaskController(taskService);
        Model model = mock(Model.class);
        String page = controller.getTodos(model, status);
        verify(model).addAttribute("tasks", tasks);
        assertThat(page).isEqualTo("todos");
    }

    @Test
    void addTask() {
        TaskService taskService = mock(TaskService.class);
        TaskController controller = new TaskController(taskService);
        String page = controller.addTask();
        assertThat(page).isEqualTo("addTask");
    }

    @Test
    void create() {
        Task task = new Task();
        TaskService taskService = mock(TaskService.class);
        when(taskService.create(task)).thenReturn(task);
        TaskController controller = new TaskController(taskService);
        String page = controller.create(task);
        verify(taskService).create(task);
        assertThat(page).isEqualTo("redirect:/todos");
    }

    @Test
    void getTaskPage() {
        Task task = new Task();
        TaskService taskService = mock(TaskService.class);
        TaskController controller = new TaskController(taskService);
        Model model = mock(Model.class);
        String page = controller.getTaskPage(model, task);
        verify(model).addAttribute("task", task);
        assertThat(page).isEqualTo("taskPage");
    }

    @Test
    void update() {
        Task task = new Task();
        TaskService taskService = mock(TaskService.class);
        TaskController controller = new TaskController(taskService);
        String page = controller.update(task);
        verify(taskService).replace(task);
        assertThat(page).isEqualTo("redirect:/todos");
    }

    @Test
    void whenTaskIsPresentThenUpdateTask() {
        int id = 0;
        Task task = new Task();
        TaskService taskService = mock(TaskService.class);
        when(taskService.findById(id)).thenReturn(Optional.of(task));
        Model model = mock(Model.class);
        TaskController controller = new TaskController(taskService);
        String page = controller.updateTask(id, model);
        verify(taskService).findById(id);
        verify(model).addAttribute("task", task);
        assertThat(page).isEqualTo("updateTask");
    }

    @Test
    void whenTaskNotFoundThenErrorPage() {
        int id = 1;
        TaskService taskService = mock(TaskService.class);
        when(taskService.findById(id)).thenReturn(Optional.empty());
        Model model = mock(Model.class);
        TaskController controller = new TaskController(taskService);
        String page = controller.updateTask(id, model);
        verify(taskService).findById(id);
        verify(model).addAttribute("error", "Не найдена задача для редактирования");
        assertThat(page).isEqualTo("error");
    }

    @Test
    void delete() {
        Task task = new Task();
        TaskService taskService = mock(TaskService.class);
        TaskController controller = new TaskController(taskService);
        String page = controller.delete(task);
        verify(taskService).delete(task.getId());
        assertThat(page).isEqualTo("redirect:/todos");
    }
}