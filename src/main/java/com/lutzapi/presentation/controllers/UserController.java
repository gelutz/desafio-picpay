package com.lutzapi.presentation.controllers;

import com.lutzapi.application.services.UserService;
import com.lutzapi.domain.entities.user.User;
import com.lutzapi.domain.entities.user.UserDTO;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {
    private UserService userService;

    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @GetMapping
    public Iterable<User> list() {
        return userService.getAllUsers();
    }

    @ResponseStatus(HttpStatus.FOUND)
    @ResponseBody
    @GetMapping("/{id}")
    public User find(@PathVariable UUID id) {
        return userService.findById(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public User create(@Valid @RequestBody User user) {
        return userService.saveUser(user);
    }

    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @PutMapping("/{id}")
    public User update(@PathVariable UUID id, @Valid @RequestBody UserDTO user) {
        return userService.updateUser(id, user);
    }
}
