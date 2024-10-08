package ru.yandex.practicum.filmorate;

import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    Gson gson;

    @Test
    public void createUserIsValid() throws Exception {
        User user = User.builder()
                .email("home@mail.ru")
                .login("login1")
                .birthday("1987-10-10")
                .build();
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(user)))
                .andExpect(status().isCreated());
    }

    @Test
    public void createUserFailedWhenEmailIsNotValid() throws Exception {
        User user = User.builder()
                .email("somethingwrongwiththis*email")
                .login("login1")
                .birthday("1987-10-10")
                .build();
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(user)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createUserFailedWhenLoginIsEmpty() throws Exception {
        User user = User.builder()
                .email("home@mail.ru")
                .birthday("1987-10-10")
                .build();
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(user)))
                .andExpect(status().isBadRequest());
    }

}
