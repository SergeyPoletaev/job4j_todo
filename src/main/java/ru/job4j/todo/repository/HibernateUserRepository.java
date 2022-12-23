package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.User;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class HibernateUserRepository implements UserRepository {
    private static final String UPDATE_USER = "update todo_user set name = :name, password = :password where id = :id";
    private static final String DELETE_USER = "delete from todo_user where id = :id";
    private static final String FROM_USER_WHERE_ID = "from todo_user where id = :id";
    private static final String SELECT_ALL_USER = "from todo_user";
    private static final String FROM_USER_WHERE_LOGIN_AND_PASSWORD
            = "from todo_user where login = :login and password = :password";
    private final CrudRepository crudRepository;

    @Override
    public User create(User user) {
        crudRepository.run(session -> session.save(user));
        return user;
    }

    @Override
    public boolean replace(User user) {
        return crudRepository.query(
                UPDATE_USER,
                Map.of("name", user.getName(),
                        "password", user.getPassword(),
                        "id", user.getId())
        );
    }

    @Override
    public boolean delete(int id) {
        return crudRepository.query(DELETE_USER, Map.of("id", id));
    }

    @Override
    public Optional<User> findById(int id) {
        return crudRepository.optional(FROM_USER_WHERE_ID, User.class, Map.of("id", id));
    }

    @Override
    public List<User> findAll() {
        return crudRepository.query(SELECT_ALL_USER, User.class);
    }

    @Override
    public Optional<User> findByLoginAndPassword(String login, String password) {
        return crudRepository.optional(FROM_USER_WHERE_LOGIN_AND_PASSWORD,
                User.class,
                Map.of("login", login, "password", password)
        );
    }
}
