package ru.job4j.todo.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.job4j.todo.model.User;
import ru.job4j.todo.service.UserService;

import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;

@Controller
@AllArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @GetMapping("/login")
    public String getLoginPage(Model model, @RequestParam(name = "fail", required = false) Boolean fail) {
        model.addAttribute("fail", fail != null);
        return "/user/login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute User user, HttpSession httpSession) {
        Optional<User> userDb = userService.findByLoginAndPassword(user.getLogin(), user.getPassword());
        if (userDb.isEmpty()) {
            return "redirect:/user/login?fail=true";
        }
        httpSession.setAttribute("user", userDb.get());
        return "redirect:/tasks/todos";
    }

    @GetMapping("/add")
    public String getRegistrationPage(Model model) {
        List<TimeZone> zones = Arrays.stream(TimeZone.getAvailableIDs())
                .map(TimeZone::getTimeZone)
                .toList();
        model.addAttribute("zones", zones);
        return "/user/add";
    }

    @PostMapping("/registration")
    public String registration(@ModelAttribute User user, RedirectAttributes redirectAttr) {
        redirectAttr.addFlashAttribute("message", "Пользователь c таким логином уже существует");
        String failRslPage = "redirect:/user/add";
        try {
            if (userService.create(user).getId() != 0) {
                return "redirect:/user/login";
            }
        } catch (Exception e) {
            return failRslPage;
        }
        return failRslPage;
    }

    @GetMapping("/logout")
    public String logout(HttpSession httpSession) {
        httpSession.invalidate();
        return "redirect:/user/login";
    }
}
