package com.lutzapi.presentation.controllers;

import com.lutzapi.domain.entities.user.User;
import com.lutzapi.application.dtos.UserDTO;
import com.lutzapi.application.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @PostMapping()
    public ResponseEntity<User> create(@RequestBody UserDTO user) {
        User createdUser = userService.createUser(user);
        return new ResponseEntity<>(createdUser, HttpStatus.OK);
    }
}
