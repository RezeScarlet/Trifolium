package com.alorisse.trifolium.controller;

import com.alorisse.trifolium.mapper.UserMapper;
import com.alorisse.trifolium.model.dto.UserResponseDTO;
import com.alorisse.trifolium.model.entity.User;
import com.alorisse.trifolium.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserMapper userMapper;

    public UserController(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> getMe(Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        return ResponseEntity.ok(userMapper.toDTO(user));
    }
}
