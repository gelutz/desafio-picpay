package com.lutzapi.presentation.controllers;

import com.lutzapi.application.dtos.UserDTO;
import com.lutzapi.application.services.UserService;
import com.lutzapi.domain.entities.user.User;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {
    private UserService userService;

    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @GetMapping
    public List<User> list() {
        return userService.getAllUsers();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    @PostMapping
    public User create(@RequestBody UserDTO user) {
        return userService.createUser(user);
    }
}
