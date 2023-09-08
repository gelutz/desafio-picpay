package com.lutzapi.presentation.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lutzapi.application.dtos.UserDTO;
import com.lutzapi.application.services.UserService;
import com.lutzapi.domain.entities.user.User;
import com.lutzapi.domain.exceptions.user.MissingDataException;
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
        List<User> users = new ArrayList<>();
        users.add(mock(User.class));
        users.add(mock(User.class));

        when(userService.getAllUsers()).thenReturn(users);
        this.mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").isNotEmpty());
    }

    @Test
    @DisplayName("Should create an User and return 201")
    public void itShouldThrowWhenCreatingUserWithMissingData() throws Exception {
        ObjectMapper om = new ObjectMapper();
        UserDTO user = mock(UserDTO.class);

        String jsonUser = om.writeValueAsString(user);

        when(userService.createUser(user)).thenThrow(MissingDataException.class);
        this.mockMvc.perform(post("/users")
                        .content(jsonUser)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
