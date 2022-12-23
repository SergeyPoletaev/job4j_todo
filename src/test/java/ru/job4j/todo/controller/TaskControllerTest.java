package ru.job4j.todo.controller;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.service.TaskService;
import ru.job4j.todo.util.HttpHelper;

import javax.servlet.http.HttpSession;
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
        String page;
        try (MockedStatic<HttpHelper> httpHelper = mockStatic(HttpHelper.class)) {
            HttpSession httpSession = mock(HttpSession.class);
            page = controller.getTodos(model, null, httpSession);
            httpHelper.verify(() -> HttpHelper.addUserToModel(httpSession, model));
        }
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
        String page;
        try (MockedStatic<HttpHelper> httpHelper = mockStatic(HttpHelper.class)) {
            HttpSession httpSession = mock(HttpSession.class);
            page = controller.getTodos(model, status, httpSession);
            httpHelper.verify(() -> HttpHelper.addUserToModel(httpSession, model));
        }
        verify(model).addAttribute("tasks", tasks);
        assertThat(page).isEqualTo("/tasks/todos");
    }

    @Test
    void addTask() {
        TaskService taskService = mock(TaskService.class);
        TaskController controller = new TaskController(taskService);
        Model model = mock(Model.class);
        String page;
        try (MockedStatic<HttpHelper> httpHelper = mockStatic(HttpHelper.class)) {
            HttpSession httpSession = mock(HttpSession.class);
            page = controller.addTask(httpSession, model);
            httpHelper.verify(() -> HttpHelper.addUserToModel(httpSession, model));
        }
        assertThat(page).isEqualTo("/tasks/add");
    }

    @Test
    void create() {
        Task task = new Task();
        TaskService taskService = mock(TaskService.class);
        when(taskService.create(task)).thenReturn(task);
        TaskController controller = new TaskController(taskService);
        HttpSession httpSession = mock(HttpSession.class);
        String page = controller.create(task, httpSession);
        verify(taskService).create(task);
        assertThat(page).isEqualTo("redirect:/tasks/todos");
    }

    @Test
    void getTaskPage() {
        Task task = new Task();
        TaskService taskService = mock(TaskService.class);
        TaskController controller = new TaskController(taskService);
        Model model = mock(Model.class);
        String page;
        try (MockedStatic<HttpHelper> httpHelper = mockStatic(HttpHelper.class)) {
            HttpSession httpSession = mock(HttpSession.class);
            page = controller.getTaskPage(model, task, httpSession);
            httpHelper.verify(() -> HttpHelper.addUserToModel(httpSession, model));
        }
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
        String page;
        try (MockedStatic<HttpHelper> httpHelper = mockStatic(HttpHelper.class)) {
            HttpSession httpSession = mock(HttpSession.class);
            page = controller.updateTask(id, model, httpSession);
            httpHelper.verify(() -> HttpHelper.addUserToModel(httpSession, model));
        }
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
        String page;
        try (MockedStatic<HttpHelper> httpHelper = mockStatic(HttpHelper.class)) {
            HttpSession httpSession = mock(HttpSession.class);
            page = controller.updateTask(id, model, httpSession);
            httpHelper.verify(() -> HttpHelper.addUserToModel(httpSession, model));
        }
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