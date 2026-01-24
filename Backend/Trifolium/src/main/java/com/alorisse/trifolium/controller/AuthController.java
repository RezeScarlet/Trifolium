package com.alorisse.trifolium.controller;

import com.alorisse.trifolium.model.dto.UserCreateRequestDTO;
import com.alorisse.trifolium.model.dto.UserLoginRequestDTO;
import com.alorisse.trifolium.model.dto.UserLoginResponseDTO;
import com.alorisse.trifolium.model.dto.UserResponseDTO;
import com.alorisse.trifolium.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    public final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@RequestBody @Valid UserCreateRequestDTO dto) {
        UserResponseDTO response = userService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponseDTO> login(@RequestBody @Valid UserLoginRequestDTO  dto) {
        UserLoginResponseDTO response = userService.login(dto);
        return  ResponseEntity.ok(response);
    }
}