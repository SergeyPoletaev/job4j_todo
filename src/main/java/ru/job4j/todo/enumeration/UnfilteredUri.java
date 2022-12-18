package ru.job4j.todo.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UnfilteredUri {
    LOGIN_PAGE("/user/login"),
    REGISTRATION_PAGE("/user/add"),
    REGISTRATION("/user/registration"),
    LOGIN("/user/login");

    private final String value;
}
