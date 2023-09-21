package com.lutzapi.presentation.controllers;

import com.lutzapi.application.dtos.UserDTO;
import com.lutzapi.application.services.UserService;
import com.lutzapi.domain.entities.user.User;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
    public User find(@PathVariable long id) {
        return userService.findById(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    @PostMapping
    public User create(@RequestBody UserDTO user) {
        return userService.createUser(user);
    }

    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @PutMapping("/{id}")
    public User create(@PathVariable Long id, @RequestBody UserDTO user) {
        return userService.updateUser(id, user);
    }


}
