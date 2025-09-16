package com.example;

import com.example.controller.UserController;
import com.example.dto.UserDto;
import com.example.mapper.UserMapper;
import com.example.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import java.time.LocalDateTime;
import java.util.List;


import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(UserController.class)
public class UserControllerTest {


    @Autowired
    private MockMvc mockMvc;


    @MockBean
    private UserService userService;


    @MockBean
    private UserMapper userMapper;

    @Test
    void getAll_returnsList() throws Exception {
        UserDto dto = new UserDto(1L, "Bob", "bob@example.com", 30, LocalDateTime.now());
        when(userService.getAllUsers()).thenReturn(List.of(dto));


        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Bob"))
                .andExpect(jsonPath("$[0].email").value("bob@example.com"));
    }


    @Test
    void update_found() throws Exception {
        UserDto dto = new UserDto(3L, "Charlie", "charlie@example.com", 28, LocalDateTime.now());
        when(userService.updateUser(eq(3L), any(), any(), anyInt())).thenReturn(dto);


        String json = "{\"name\":\"Charlie\",\"email\":\"charlie@example.com\",\"age\":28}";


        mockMvc.perform(put("/api/users/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3L))
                .andExpect(jsonPath("$.name").value("Charlie"));
    }


    @Test
    void update_notFound() throws Exception {
        when(userService.updateUser(eq(99L), any(), any(), anyInt())).thenReturn(null);


        String json = "{\"name\":\"Ghost\",\"email\":\"ghost@example.com\",\"age\":40}";


        mockMvc.perform(put("/api/users/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound());
    }


    @Test
    void delete_found() throws Exception {
        when(userService.deleteUser(1L)).thenReturn(true);


        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isNoContent());
    }


    @Test
    void delete_notFound() throws Exception {
        when(userService.deleteUser(42L)).thenReturn(false);


        mockMvc.perform(delete("/api/users/42"))
                .andExpect(status().isNotFound());
    }
}