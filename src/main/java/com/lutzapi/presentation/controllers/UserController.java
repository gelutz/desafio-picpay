package com.lutzapi.presentation.controllers;

import com.lutzapi.application.services.UserService;
import com.lutzapi.domain.entities.user.User;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasRole('BUYER') or hasRole('SELLER')")
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
        return userService.createUser(user);
    }

    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @PostMapping("/seed")
    public User seed() {
        return userService.seedDatabase();
    }
}
