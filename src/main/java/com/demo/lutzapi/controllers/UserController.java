package com.demo.lutzapi.controllers;

import com.demo.lutzapi.domain.user.User;
import com.demo.lutzapi.dtos.UserDTO;
import com.demo.lutzapi.repositories.UserRepository;
import com.demo.lutzapi.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.net.http.HttpResponse;
import java.util.List;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> list() {
        try {
            List<User> users = userService.getAllUsers();

            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping()
    public ResponseEntity<User> create(@RequestBody UserDTO user) {
        try {
            User createdUser = userService.createUser(user);

            return new ResponseEntity<>(createdUser, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
