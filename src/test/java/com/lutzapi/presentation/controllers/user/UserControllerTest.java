package com.lutzapi.presentation.controllers.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lutzapi.application.dtos.UserDTO;
import com.lutzapi.application.services.UserService;
import com.lutzapi.domain.entities.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


// depois de alguns testes descobri que essa classe não testa o @ControllerAdvice
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserControllerTest {
    @MockBean
    private UserService userService;
    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("The endpoint should return an empty list when there are no users")
    public void itShouldReturnAnEmptyArray() throws Exception {
        this.mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    @DisplayName("The endpoint should return a list of existing users")
    public void itShouldReturnAListOfUsers() throws Exception {
        User mockUser1 = mock(User.class);
        User mockUser2 = mock(User.class);
        when(mockUser1.getId()).thenReturn(1L);
        when(mockUser2.getId()).thenReturn(2L);

        List<User> users = new ArrayList<>();
        users.add(mockUser1);
        users.add(mockUser2);

        when(userService.getAllUsers()).thenReturn(users);
        this.mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").isNotEmpty())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));
    }

    @Test
    @DisplayName("Should create an User and return 201")
    public void itShouldThrowWhenCreatingUserWithMissingData() throws Exception {
        ObjectMapper om = new ObjectMapper();
        UserDTO user = mock(UserDTO.class);

        String jsonUser = om.writeValueAsString(user);

        this.mockMvc.perform(post("/users")
                        .content(jsonUser)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }
}
