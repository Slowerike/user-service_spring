package com.example.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;


public record UserCreateRequest(
        @NotBlank(message = "Имя не должно быть пустым") String name,
        @Email(message = "Некорректный email") String email,
        @Min(value = 0, message = "Возраст не может быть отрицательным") int age
) {}