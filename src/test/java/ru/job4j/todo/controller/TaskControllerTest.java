package ru.job4j.todo.controller;

import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.service.TaskService;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class TaskControllerTest {

    @Test
    void whenGetTodosParamFree() {
        List<Task> tasks = List.of(new Task(), new Task());
        TaskService taskService = mock(TaskService.class);
        when(taskService.findAll()).thenReturn(tasks);
        TaskController controller = new TaskController(taskService);
        Model model = mock(Model.class);
        String page = controller.getTodos(model, null);
        verify(model).addAttribute("tasks", tasks);
        assertThat(page).isEqualTo("/tasks/todos");
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
        assertThat(page).isEqualTo("/tasks/todos");
    }

    @Test
    void addTask() {
        TaskService taskService = mock(TaskService.class);
        TaskController controller = new TaskController(taskService);
        String page = controller.addTask();
        assertThat(page).isEqualTo("/tasks/add");
    }

    @Test
    void create() {
        Task task = new Task();
        TaskService taskService = mock(TaskService.class);
        when(taskService.create(task)).thenReturn(task);
        TaskController controller = new TaskController(taskService);
        String page = controller.create(task);
        verify(taskService).create(task);
        assertThat(page).isEqualTo("redirect:/tasks/todos");
    }

    @Test
    void getTaskPage() {
        Task task = new Task();
        TaskService taskService = mock(TaskService.class);
        TaskController controller = new TaskController(taskService);
        Model model = mock(Model.class);
        String page = controller.getTaskPage(model, task);
        verify(model).addAttribute("task", task);
        assertThat(page).isEqualTo("/tasks/todo");
    }

    @Test
    void whenUpdateSuccess() {
        Task task = new Task();
        TaskService taskService = mock(TaskService.class);
        when(taskService.replace(task)).thenReturn(true);
        TaskController controller = new TaskController(taskService);
        RedirectAttributes redirectAttr = mock(RedirectAttributes.class);
        String page = controller.update(task, redirectAttr);
        verify(taskService).replace(task);
        assertThat(page).isEqualTo("redirect:/tasks/todos");
    }

    @Test
    void whenUpdateFail() {
        Task task = new Task();
        TaskService taskService = mock(TaskService.class);
        TaskController controller = new TaskController(taskService);
        RedirectAttributes redirectAttr = mock(RedirectAttributes.class);
        String page = controller.update(task, redirectAttr);
        verify(taskService).replace(task);
        verify(redirectAttr).addFlashAttribute("message", "Обновить задачу не получилось!");
        assertThat(page).isEqualTo("redirect:/shared/fail");
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
        assertThat(page).isEqualTo("/tasks/formUpdate");
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
        verify(model).addAttribute("message", "Не найдена задача для редактирования");
        assertThat(page).isEqualTo("/shared/fail");
    }

    @Test
    void whenDeleteSuccess() {
        Task task = new Task();
        TaskService taskService = mock(TaskService.class);
        when(taskService.delete(task.getId())).thenReturn(true);
        TaskController controller = new TaskController(taskService);
        RedirectAttributes redirectAttr = mock(RedirectAttributes.class);
        String page = controller.delete(task, redirectAttr);
        verify(taskService).delete(task.getId());
        assertThat(page).isEqualTo("redirect:/tasks/todos");
    }

    @Test
    void whenDeleteFail() {
        Task task = new Task();
        TaskService taskService = mock(TaskService.class);
        TaskController controller = new TaskController(taskService);
        RedirectAttributes redirectAttr = mock(RedirectAttributes.class);
        String page = controller.delete(task, redirectAttr);
        verify(taskService).delete(task.getId());
        verify(redirectAttr).addFlashAttribute("message", "Удалить задачу не получилось!");
        assertThat(page).isEqualTo("redirect:/shared/fail");
    }
}