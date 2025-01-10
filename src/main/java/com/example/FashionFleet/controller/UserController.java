package com.example.FashionFleet.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.FashionFleet.domain.User;
import com.example.FashionFleet.service.UserService;

@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("user/create")
    public String createNewUser() {
        User user = new User();
        user.setEmail("thienvovinpro123@gmail.com");
        user.setName("Thien");
        user.setPassword("123456");
        this.userService.handleCreateUser(user);
        return "success";
    }
}
