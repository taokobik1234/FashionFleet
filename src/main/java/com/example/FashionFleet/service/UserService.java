package com.example.FashionFleet.service;

import org.springframework.stereotype.Service;

import com.example.FashionFleet.domain.User;
import com.example.FashionFleet.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void handleCreateUser(User user) {
        this.userRepository.save(user);
    }
}
