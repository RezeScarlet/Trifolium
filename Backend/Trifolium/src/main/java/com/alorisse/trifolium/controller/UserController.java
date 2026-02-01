package com.alorisse.trifolium.controller;

import com.alorisse.trifolium.mapper.UserMapper;
import com.alorisse.trifolium.model.dto.UserResponseDTO;
import com.alorisse.trifolium.model.dto.UserUpdateRequestDTO;
import com.alorisse.trifolium.model.entity.User;
import com.alorisse.trifolium.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserMapper userMapper;

    private final UserService userService;

    public UserController(UserMapper userMapper, UserService userService) {
        this.userMapper = userMapper;
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> getMe(Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        assert user != null;

        return ResponseEntity.ok(userMapper.toDTO(user));
    }

    @PutMapping("/me")
    public ResponseEntity<UserResponseDTO> updatedMe(@RequestBody @Valid UserUpdateRequestDTO dto, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        UserResponseDTO response = userService.update(user, dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteMe(Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        userService.delete(user);

        return ResponseEntity.noContent().build();
    }

}
