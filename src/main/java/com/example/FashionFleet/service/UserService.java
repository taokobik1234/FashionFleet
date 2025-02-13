package com.example.FashionFleet.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.FashionFleet.domain.User;
import com.example.FashionFleet.domain.dto.response.user.ResCreateUserDTO;
import com.example.FashionFleet.domain.dto.response.user.ResUpdateUserDTO;
import com.example.FashionFleet.domain.dto.response.user.ResUserDTO;
import com.example.FashionFleet.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User handleCreateUser(User user) {
        return this.userRepository.save(user);
    }

    public boolean isEmailExist(String email) {
        return this.userRepository.existsByEmail(email);
    }

    public User handleGetUserByUsername(String username) {
        return this.userRepository.findByEmail(username);
    }

    public void updateUserToken(String token, String email) {
        User currentUser = this.handleGetUserByUsername(email);
        if (currentUser != null) {
            currentUser.setRefreshToken(token);
            this.userRepository.save(currentUser);
        }
    }

    public User fetchUserById(long id) {
        Optional<User> userDB = this.userRepository.findById(id);
        if (userDB.isPresent()) {
            return userDB.get();
        }
        return null;
    }

    public ResCreateUserDTO convertToResCreateUserDTO(User user) {
        ResCreateUserDTO res = new ResCreateUserDTO();
        res.setId(user.getId());
        res.setAddress(user.getAddress());
        res.setPhoneNumber(user.getPhoneNumber());
        res.setAge(user.getAge());
        res.setName(user.getName());
        res.setEmail(user.getEmail());
        res.setCreatedAt(user.getCreatedAt());
        return res;
    }

    public User handleUpdateUser(User user) {
        User userDb = this.fetchUserById(user.getId());
        if (userDb != null) {
            userDb.setAddress(user.getAddress());
            userDb.setAge(user.getAge());
            userDb.setName(user.getName());
            userDb.setPhoneNumber(user.getPhoneNumber());
            userDb = this.userRepository.save(userDb);
        }
        return userDb;
    }

    public ResUpdateUserDTO convertToResUpdateUserDTO(User user) {
        ResUpdateUserDTO resUpdateUserDTO = new ResUpdateUserDTO();
        resUpdateUserDTO.setId(user.getId());
        resUpdateUserDTO.setAddress(user.getAddress());
        resUpdateUserDTO.setName(user.getName());
        resUpdateUserDTO.setAge(user.getAge());
        resUpdateUserDTO.setPhoneNumber(user.getPhoneNumber());
        return resUpdateUserDTO;
    }

    public void handleDeleteUser(long id) {
        this.userRepository.deleteById(id);
    }

    public ResUserDTO convertToResUserDTO(User user) {
        ResUserDTO resUserDTO = new ResUserDTO();
        resUserDTO.setId(user.getId());
        resUserDTO.setAddress(user.getAddress());
        resUserDTO.setName(user.getName());
        resUserDTO.setAge(user.getAge());
        resUserDTO.setPhoneNumber(user.getPhoneNumber());
        resUserDTO.setEmail(user.getEmail());
        return resUserDTO;
    }
}
