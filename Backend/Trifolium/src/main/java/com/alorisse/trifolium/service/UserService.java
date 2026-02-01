package com.alorisse.trifolium.service;

import com.alorisse.trifolium.mapper.UserMapper;
import com.alorisse.trifolium.model.dto.*;
import com.alorisse.trifolium.model.entity.User;
import com.alorisse.trifolium.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final TokenService tokenService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, UserMapper userMapper, TokenService tokenService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
        this.tokenService = tokenService;
    }

    public UserResponseDTO create(UserCreateRequestDTO dto) {
        if (userRepository.existsByEmail(dto.email())){
            throw new RuntimeException("Email already in use.");
        }
        User user = userMapper.toEntity(dto);
        String hashString = passwordEncoder.encode(dto.password());
        assert hashString != null; // TODO deal with exceptions
        user.setPassword(hashString.getBytes(StandardCharsets.UTF_8));

        User saved = userRepository.save(user);

        return userMapper.toDTO(saved);
    }

    public UserLoginResponseDTO login(UserLoginRequestDTO dto) {
        User user = userRepository.findByEmail(dto.email()).orElseThrow(() -> new RuntimeException("Invalid user or password."));

        if (!validatePassword(dto.password(), user.getPassword())){
            throw new RuntimeException("Invalid user or password.");
        }

        String token = tokenService.generateToken(user);

        return new UserLoginResponseDTO(token, user.getUsername(), user.getEmail());
    }

    public UserResponseDTO update(User user, UserUpdateRequestDTO dto) {
        if (dto.username() != null && !dto.username().isBlank()) {
            user.setUsername(dto.username());
        }

        if (dto.email() != null && !dto.email().isBlank()) {
            if (!user.getEmail().equals(dto.email()) && userRepository.existsByEmail(dto.email())){
                throw new RuntimeException("Email already in use.");
            }
            user.setEmail(dto.email());
        }

        if (dto.currency() != null && !dto.currency().isBlank()) {
            user.setCurrency(dto.currency());
        }

        if (dto.password() != null && !dto.password().isBlank()) {
            if (dto.oldPassword() == null || !validatePassword(dto.oldPassword(), user.getPassword())) {
                throw new RuntimeException("Invalid current password");
            }

            String hashString = passwordEncoder.encode(dto.password());
            assert hashString != null;
            user.setPassword(hashString.getBytes(StandardCharsets.UTF_8));
        }

        User updated = userRepository.save(user);

        return userMapper.toDTO(updated);
    }


    public void delete(User user) {
        userRepository.delete(user);
    }

    // TODO Create auth code
    public UserResponseDTO createByProvider(UserProviderAuthRequestDTO dto) {
        throw new RuntimeException("Not implemented yet.");
    }

    public boolean validatePassword(String password, byte[] passwordHash) {
        String hash = new String(passwordHash, StandardCharsets.UTF_8);
        
        return passwordEncoder.matches(password, hash);
    }
}
