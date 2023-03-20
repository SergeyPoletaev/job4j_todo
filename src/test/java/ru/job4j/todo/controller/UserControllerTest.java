package ru.job4j.todo.controller;

import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.job4j.todo.model.User;
import ru.job4j.todo.service.UserService;

import javax.servlet.http.HttpSession;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Test
    void whenGetLoginPage() {
        UserService userService = mock(UserService.class);
        UserController userController = new UserController(userService);
        Model model = mock(Model.class);
        String page = userController.getLoginPage(model, null);
        verify(model).addAttribute("fail", false);
        assertThat(page).isEqualTo("/user/login");
    }

    @Test
    void whenLoginSuccess() {
        User user = new User();
        Optional<User> userDb = Optional.of(new User());
        UserService userService = mock(UserService.class);
        when(userService.findByLoginAndPassword(user.getLogin(), user.getPassword())).thenReturn(userDb);
        UserController userController = new UserController(userService);
        HttpSession httpSession = mock(HttpSession.class);
        String page = userController.login(user, httpSession);
        verify(httpSession).setAttribute("user", userDb.get());
        assertThat(page).isEqualTo("redirect:/tasks/todos");
    }

    @Test
    void whenLoginFail() {
        User user = new User();
        Optional<User> userDb = Optional.empty();
        UserService userService = mock(UserService.class);
        when(userService.findByLoginAndPassword(user.getLogin(), user.getPassword())).thenReturn(userDb);
        UserController userController = new UserController(userService);
        HttpSession httpSession = mock(HttpSession.class);
        String page = userController.login(user, httpSession);
        assertThat(page).isEqualTo("redirect:/user/login?fail=true");
    }

    @Test
    void getRegistrationPage() {
        UserService userService = mock(UserService.class);
        UserController userController = new UserController(userService);
        Model model = mock(Model.class);
        assertThat(userController.getRegistrationPage(model)).isEqualTo("/user/add");
    }

    @Test
    void whenRegistrationSuccess() {
        User user = new User();
        user.setId(1);
        UserService userService = mock(UserService.class);
        when(userService.create(user)).thenReturn(user);
        UserController userController = new UserController(userService);
        RedirectAttributes redirectAttr = mock(RedirectAttributes.class);
        String page = userController.registration(user, redirectAttr);
        assertThat(page).isEqualTo("redirect:/user/login");
    }

    @Test
    void whenRegistrationFail() {
        User user = new User();
        UserService userService = mock(UserService.class);
        when(userService.create(user)).thenReturn(user);
        UserController userController = new UserController(userService);
        RedirectAttributes redirectAttr = mock(RedirectAttributes.class);
        String page = userController.registration(user, redirectAttr);
        verify(redirectAttr).addFlashAttribute("message", "Пользователь c таким логином уже существует");
        assertThat(page).isEqualTo("redirect:/user/add");
    }

    @Test
    void logout() {
        UserService userService = mock(UserService.class);
        UserController userController = new UserController(userService);
        HttpSession httpSession = mock(HttpSession.class);
        String page = userController.logout(httpSession);
        verify(httpSession).invalidate();
        assertThat(page).isEqualTo("redirect:/user/login");
    }
}