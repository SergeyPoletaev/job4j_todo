package ru.job4j.todo.util;


import lombok.experimental.UtilityClass;
import org.springframework.ui.Model;
import ru.job4j.todo.model.User;

import javax.servlet.http.HttpSession;

@UtilityClass
public class HttpHelper {

    public void addUserToModel(HttpSession httpSession, Model model) {
        User user = getUser(httpSession);
        model.addAttribute("user", user);
    }

    private User getUser(HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");
        if (user == null) {
            user = new User();
            user.setName("Гость");
        }
        return user;
    }
}
