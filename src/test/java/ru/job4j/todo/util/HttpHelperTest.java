package ru.job4j.todo.util;

import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;
import ru.job4j.todo.model.User;

import javax.servlet.http.HttpSession;

import static org.mockito.Mockito.*;

class HttpHelperTest {

    @Test
    void whenAddUserToModel() {
        User user = new User();
        Model model = mock(Model.class);
        HttpSession httpSession = mock(HttpSession.class);
        when((User) httpSession.getAttribute("user")).thenReturn(user);
        HttpHelper.addUserToModel(httpSession, model);
        verify(model).addAttribute("user", user);
    }
}