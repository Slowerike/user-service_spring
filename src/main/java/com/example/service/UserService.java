package com.example.service;

import com.example.dto.UserDto;
import com.example.mapper.UserMapper;
import com.example.model.User;
import com.example.repository.UserRepository;
import org.springframework.stereotype.Service;


import java.util.List;


@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toDto)
                .toList();
    }

    public UserDto getUserById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toDto)
                .orElse(null);
    }

    public UserDto createUser(String name, String email, int age) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Пользователь с таким email уже существует");
        }
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setAge(age);
        User saved = userRepository.save(user);
        return userMapper.toDto(saved);
    }

    public UserDto updateUser(Long id, String name, String email, int age) {
        return userRepository.findById(id)
                .map(user -> {
                    userRepository.findByEmail(email)
                            .filter(u -> !u.getId().equals(id))
                            .ifPresent(u -> {
                                throw new IllegalArgumentException("Email уже занят другим пользователем");
                            });

                    user.setName(name);
                    user.setEmail(email);
                    user.setAge(age);
                    return userMapper.toDto(userRepository.save(user));
                })
                .orElse(null);
    }

    public boolean deleteUser(Long id) {
        if (!userRepository.existsById(id)) return false;
        userRepository.deleteById(id);
        return true;
    }
}