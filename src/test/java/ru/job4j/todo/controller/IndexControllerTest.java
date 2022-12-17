package ru.job4j.todo.controller;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class IndexControllerTest {

    @Test
    void getIndex() {
        IndexController controller = new IndexController();
        String page = controller.getIndex();
        assertThat(page).isEqualTo("redirect:/tasks/todos");
    }
}