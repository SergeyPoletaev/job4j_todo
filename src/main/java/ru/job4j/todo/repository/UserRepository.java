package ru.job4j.todo.repository;

import ru.job4j.todo.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    User create(User user);

    boolean replace(User user);

    boolean delete(int id);

    Optional<User> findById(int id);

    List<User> findAll();

    Optional<User> findByLoginAndPassword(String login, String password);
}
