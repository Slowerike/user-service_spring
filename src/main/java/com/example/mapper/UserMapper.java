package com.example.mapper;

import com.example.dto.UserDto;
import com.example.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDto toDto(User user) {
        if (user == null) return null;
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getAge(),
                user.getCreatedAt()
        );
    }

    public User toEntity(UserDto dto) {
        if (dto == null) return null;
        User user = new User();
        user.setName(dto.name());
        user.setEmail(dto.email());
        user.setAge(dto.age());
        return user;
    }

    public void updateEntity(UserDto dto, User user) {
        if (dto == null || user == null) return;
        user.setName(dto.name());
        user.setEmail(dto.email());
        user.setAge(dto.age());
    }
}
