package ru.yandex.practicum.filmorate;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class UserControllerTest {

    @Autowired
    UserController controller;

    @Test
    public void shouldReturnAllUsers() {
        User user = User.builder()
                .email("home@mail.ru")
                .login("login1")
                .birthday("1987-10-10")
                .build();
        assertEquals(0, controller.findAll().size());
        controller.create(user);
        log.info(controller.findAll().toString());
        assertEquals(1, controller.findAll().size());
        User user1 = User.builder()
                .email("homemail.ru")
                .login("")
                .birthday("1987-10-10")
                .build();
        controller.create(user1);

    }

    @Test
    public void testUserValidation() {
    }
}
